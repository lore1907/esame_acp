package client;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.*;

public class Client {
    
    public static final int NUM_THREADS = 5; 
    private static final Proxy proxy = new Proxy("localhost", 8000);


    public static void main(String[] args){

        ClientThread[] threads = new ClientThread[NUM_THREADS]; 

        try{

            for(int i=0; i<NUM_THREADS; i++){
                threads[i] = new ClientThread(proxy);
                threads[i].start();
            }

            for(ClientThread t: threads) t.join();

            Hashtable<String, String> prop = new Hashtable<String, String>();
            prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            prop.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            prop.put("queue.booking", "booking");

            QueueConnection qconn = null;
            QueueSession qsession = null;
            QueueSender qsender = null;

            try {
                Context ctx = new InitialContext(prop);
                QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
                Queue booking = (Queue) ctx.lookup("booking");
                qconn = qcf.createQueueConnection();
                qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                qsender = qsession.createSender(booking);
                TextMessage msg = qsession.createTextMessage("STOP");
                qsender.send(msg);

            } catch (NamingException e){
                e.printStackTrace();
            } catch (JMSException e){
                e.printStackTrace();
            } finally{
                if(qsender!=null) qsender.close();
                if(qsession!=null) qsession.close();
                if(qconn!=null) qconn.close();
            }

        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (JMSException e){
            e.printStackTrace();
        }
    }
}
