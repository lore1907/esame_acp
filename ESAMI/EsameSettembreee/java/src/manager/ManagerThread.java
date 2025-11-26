package manager;
import javax.naming.*;

import java.util.Hashtable;

import javax.jms.*;
import generated.*;


public class ManagerThread extends Thread{

    private final DeployReq dreq; 
    private final StopReq sreq; 

    public ManagerThread(DeployReq req){
        this.dreq = req; 
        this.sreq = null;
    }

    public ManagerThread(StopReq req){
        this.sreq = req;
        this.dreq = null; 
    }

    @Override
    public void run() {
        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.gpu", "gpu");
        prop.put("topic.rt", "rt");

        try{
            Context jndiContext = new InitialContext(prop);
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            
            String topicName;
            String messageText; 

            if (dreq != null){
                String taskType = dreq.getTask(); 
                if ("real-time".equals(taskType)) {
                    topicName = "rt";
                } else {
                    topicName = "gpu";
                }
                messageText = "deploy-" + dreq.getId() + "-" + dreq.getName();
            } else if (sreq != null) {
                String taskType = dreq.getTask(); 
                if ("real-time".equals(taskType)) {
                    topicName = "rt";
                } else {
                    topicName = "gpu";
                }
                messageText = "stop_all"; 
            } else {
                System.out.println("Richiesta non valida nel thread");
                return;
            }

            Topic topic = (Topic) jndiContext.lookup(topicName); 
            TopicConnection tconn = tcf.createTopicConnection(); 
            tconn.start(); 

            TopicSession ts = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE); 
            TopicPublisher tpub = ts.createPublisher(topic); 
            TextMessage message = ts.createTextMessage(messageText); 

            tpub.send(message); 
            System.out.println("Messaggio inviato."); 

            tpub.close(); 
            ts.close();
            tconn.close(); 
        } catch (NamingException | JMSException e){
            System.out.println("Errore durante l'invio del messaggio."); 
        }
    }
}
