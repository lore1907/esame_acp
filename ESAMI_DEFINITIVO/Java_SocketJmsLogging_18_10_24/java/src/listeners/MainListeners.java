package listeners;
import java.util.Hashtable;


import javax.jms.*;
import javax.naming.*;

public class MainListeners {
 
    public static void main(String[] args){

        System.out.println("[MAIN-LISTENERS] Listener in avvio...");
        Hashtable <String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        prop.put("queue.info", "info");
        prop.put("queue.error", "error"); 

        try{
            Context ctx = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue info = (Queue) ctx.lookup("info");
            Queue error = (Queue) ctx.lookup("error");
            
            QueueConnection qc = qcf.createQueueConnection();
            qc.start(); 

            QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver infoReceiver = qs.createReceiver(info); 
            QueueReceiver errorReceiver = qs.createReceiver(error);

            ErrorChecker listener_errors = new ErrorChecker(args[0]);
            errorReceiver.setMessageListener(listener_errors);

        }catch (JMSException e){
            e.printStackTrace();
        }

    }
}
