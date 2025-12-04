package manager;
import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;

public class Manager {
    

    public static void main(String[] args){

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.stats", "stats");
        prop.put("topic.tickets", "tickets");
        prop.put("topic.request", "request"); 

        try{ 
            Context ctx = new InitialContext(prop); 
            TopicConnectionFactory tcf = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory"); 
            
            Topic topic_stats = (Topic) ctx.lookup("stats");
            Topic topic_tickets = (Topic) ctx.lookup("tickets");
            Topic request = (Topic) ctx.lookup("request");

            TopicConnection tconn = tcf.createTopicConnection();
            tconn.start();
            System.out.println("Manager avviato. In attesa di richieste...."); 
            TopicSession ts = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            TopicPublisher tpub_stats = ts.createPublisher(topic_stats);
            TopicPublisher tpub_tickets = ts.createPublisher(topic_tickets);
            TopicSubscriber tsub = ts.createSubscriber(request);

            ManagerListener listener = new ManagerListener(ts, tpub_stats, tpub_tickets);
            tsub.setMessageListener(listener);

            while(true){
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
