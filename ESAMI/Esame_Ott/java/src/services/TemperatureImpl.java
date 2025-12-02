package services;
import javax.jms.*;
import coda.*;
import codaimpl.*;

public class TemperatureImpl implements ITemperature{
    
    private CodaWrapper codaWrapper; 

    public TemperatureImpl(int size, QueueSession qs, Queue qLow, Queue qMid, Queue qHigh){
        CodaCircolare coda = new CodaCircolare(size);
        codaWrapper = new CodaWrapperLock(coda); 
        Consumatore consumer = new Consumatore(codaWrapper, qs, qLow, qMid, qHigh);
        consumer.start();
    }

    @Override
    public void temp(float valore, int tipo){
        String request = valore + "-" + tipo;
        Thread produttore = new myThread(request);
        produttore.start();
    }

    private class myThread extends Thread{
        private String request;

        public myThread(String r){
            request = r;
        }

        @Override
        public void run(){
            codaWrapper.inserisci(request);
        }
    }

    private class Consumatore extends Thread{

        private final CodaWrapper coda; 
        private final QueueSession session; 
        private final Queue qLow;
        private final Queue qMid;
        private final Queue qHigh; 
        
        private Consumatore(CodaWrapper c, QueueSession qs, Queue ql, Queue qm, Queue qh){
            coda = c;
            session = qs;
            qLow = ql;
            qMid = qm;
            qHigh = qh;
        }

        @Override
        public void run(){

            try{
                QueueSender senderLow = session.createSender(qLow);
                QueueSender senderMid = session.createSender(qMid);
                QueueSender senderHigh = session.createSender(qHigh);
                
                while(true){
                    String msg = coda.preleva(); 
                    String parts[] = msg.split("-"); 

                    if (parts.length != 2)
                        continue; 

                    String valore = parts[0];
                    int tipo; 

                    try{
                        tipo = Integer.parseInt(parts[1]);
                    } catch(NumberFormatException e){
                        e.printStackTrace();
                        continue;
                    }

                    TextMessage jmsMess = session.createTextMessage(msg);
                    if (tipo == 0) {
                        senderLow.send(jmsMess);
                    } else if (tipo == 1) {
                        senderMid.send(jmsMess);
                    } else if (tipo == 2) {
                        senderHigh.send(jmsMess);
                    } else {
                        System.out.println("[CONSUMER THREAD] tipo sconosciuto.");
                    }
                }
            
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
