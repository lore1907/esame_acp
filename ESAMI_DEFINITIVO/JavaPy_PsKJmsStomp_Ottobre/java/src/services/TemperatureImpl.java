package services;
import coda.*;
import codaimpl.*;
import javax.jms.*;

public class TemperatureImpl implements ITemperature{

    private int size;
    private CodaWrapper codaWrapper;

    public TemperatureImpl(int s, QueueConnection qconn, Queue low, Queue mid, Queue high){
        size = s;
        CodaCircolare coda = new CodaCircolare(size);
        codaWrapper = new CodaWrapperLock(coda);
        Consumatore cons = new Consumatore(codaWrapper, qconn, low, mid, high);
        cons.start();

    }

    @Override
    public void temp(float valore, int tipo) {
        System.out.println("Invocato metodo temp con i seguenti parametri: VALORE-" + valore +  "\t TIPO-" + tipo);
        System.out.println("Avvio produttore....");
        Produttore prod = new Produttore(valore, tipo);
        prod.start();
    }

    private class Produttore extends Thread{

        private float valore; 
        private int tipo;
        
        private Produttore(float valore, int tipo){
            this.valore = valore;
            this.tipo = tipo;
        }

        public void run(){
            String misurazione = valore + "-" + tipo;
            System.out.println("Thread produttore in esecuzione, inserisco " + misurazione + " in coda");
            codaWrapper.inserisci(misurazione);
        }

    }

    private class Consumatore extends Thread{

        private QueueConnection qconn;
        private Queue low;
        private Queue mid;
        private Queue high;

        private Consumatore(CodaWrapper cw, QueueConnection qconn, Queue low, Queue mid, Queue high){
            codaWrapper = cw;
            this.qconn = qconn;
            this.low = low;
            this.mid = mid;
            this.high = high;
        }

        public void run(){

            QueueSession qSession = null;
            QueueSender low_sender = null;
            QueueSender mid_sender = null;
            QueueSender high_sender = null;
            
            try {
                qSession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                low_sender = qSession.createSender(low);
                mid_sender = qSession.createSender(mid);
                high_sender = qSession.createSender(high);
                TextMessage msg;

                
                while(true){
                    String misurazione = codaWrapper.preleva();
                    // devo passare sessione e code e creare il messaggio verso stomp (( le creo nel server e le passo quando avvio lo skeleton))
                    String severity = misurazione.split("-")[1];
                    msg = qSession.createTextMessage(misurazione);
                    
                    if (severity.equalsIgnoreCase("0")){
                        low_sender.send(msg);
                    } else if (severity.equalsIgnoreCase("1")){
                        mid_sender.send(msg);
                    } else if (severity.equalsIgnoreCase("2")){
                        high_sender.send(msg);
                    } else {
                        System.err.println("Errore, severita' non riconosciuta = " + severity);
                    }

                }
               
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                System.out.println("Richiesta chiusura da tastiera... chiudo le risorse");
                try{
                    
                    if(low_sender!= null) {low_sender.close();}
                    if(low_sender!= null) {mid_sender.close();}
                    if(low_sender!= null) {high_sender.close();}
                    if(qSession!= null)   {qSession.close();}
                }catch(JMSException e){
                    e.printStackTrace();
                }
                
            }
            
        }

    }

}
