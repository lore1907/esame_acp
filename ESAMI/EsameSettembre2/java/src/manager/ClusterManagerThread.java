package manager;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;
import javax.naming.*;

import org.apache.activemq.management.JMSEndpointStatsImpl;

import generated.DeployRequest;
import generated.StopRequest;

public class ClusterManagerThread extends Thread{
    
    private static final Logger logger = Logger.getLogger(ClusterManagerThread.class.getName());
    private final DeployRequest dreq;
    private final StopRequest sreq;

    public ClusterManagerThread(DeployRequest req){
        this.dreq = req;
        this.sreq = null;
    }

    public ClusterManagerThread(StopRequest req){ 
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

        try {
            Context jndiContext = new InitialContext();
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");

            String topicName;
            String messageText;

            if(dreq != null) {
                String taskType = dreq.getTaskType();
                if ("real-time".equals(taskType)) {
                    topicName = "rt";
                } else {
                    topicName = "gpu";
                }
                messageText = "deploy-" + dreq.getId() +  "-" + dreq.getName();
            } else if (sreq != null) {
                String taskType = dreq.getTaskType(); 
                if ("real-time".equals(taskType)) {
                    topicName = "rt";
                } else {
                    topicName = "gpu";
                }
                messageText = "stop_all";
            } else {
                logger.log(Level.SEVERE, "Richiesta nonn valida nel thread.");
                return;
            }

            Topic topic = (Topic) jndiContext.lookup(topicName);

            TopicConnection tconn = tcf.createTopicConnection();
            tconn.start();

            TopicSession ts = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicPublisher tpub = ts.createPublisher(topic);
            TextMessage message = ts.createTextMessage(messageText);
            
            tpub.send(message);
            logger.log(Level.INFO, "Messaggio inviato");

            tpub.close();
            ts.close();
            tconn.close();
        } catch (NamingException | JMSException e) {
            logger.log(Level.SEVERE, "Errore durante l invio del messaggio");
        }
    }
}
