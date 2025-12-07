package dispatcher;
import javax.jms.*;

import org.apache.activemq.management.JMSEndpointStatsImpl;


public class DispatcherThread extends Thread{
    
    private  QueueConnection qconn;
    private Dispatcher dispatcher; 
    private TextMessage msg;
    private String parsed_msg;


    protected DispatcherThread(QueueConnection qconn, Dispatcher dispatcher,TextMessage msg){
        this.qconn = qconn;
        this.dispatcher = dispatcher;
        this.msg = msg;
        try {
            parsed_msg = msg.getText();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void run(){

        QueueSession qsession=null;
        QueueSender qsender=null;
        
        try{
            qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            qsender = qsession.createSender((Queue)msg.getJMSReplyTo());
            TextMessage msg_to_send;

            if(parsed_msg.contains("deposita")){
                long id_articolo = Integer.parseInt(parsed_msg.split("-")[1]);
                dispatcher.deposita(id_articolo);
                msg_to_send = qsession.createTextMessage("deposited");
                
            } else if(parsed_msg.contains("preleva")){
                long id_articolo = dispatcher.preleva();
                msg_to_send = qsession.createTextMessage(String.valueOf(id_articolo));
            } else {
                System.out.println("Richiesta non riconosciuta. Errore!");
                return;
            }

            qsender.send(msg_to_send);
            Thread.sleep(1000);

        }catch (JMSException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
        } finally {
            
            try{
                if(qsender!=null)
                    qsender.close();
                if(qsession!=null)
                    qsession.close();
            }catch(JMSException e){
                e.printStackTrace();
            }

        }
    }
}
