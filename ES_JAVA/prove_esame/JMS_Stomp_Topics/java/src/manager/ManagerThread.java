package manager;

import javax.jms.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.*;


public class ManagerThread extends Thread{
    
    private TopicConnection tconn;
    private MapMessage mapMessage;
    private Topic topic_stats; 
    private Topic topic_tickets; 

    private static final Logger logger = Logger.getLogger(ManagerThread.class.getName());
    private static final Object TICKET_LOCK  = new Object();

    public ManagerThread (TopicConnection tc, Topic t1, Topic t2, MapMessage m) {
        tconn = tc;
        topic_stats = t1;
        topic_tickets = t2;
        mapMessage = m;
    }


    public void run() {

        logger.info("**** Manager Thread avviato ****");
        
        try {

            TopicSession  topicSession = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            String tipo = mapMessage.getString("type");
            String value = mapMessage.getString("value");
            TextMessage msg = topicSession.createTextMessage();


            if (tipo.equalsIgnoreCase("stats")) {
                
                TopicPublisher publisher = topicSession.createPublisher(topic_stats);
                msg.setText(value);
                publisher.publish(msg);
                publisher.close();

                logger.log(Level.INFO, "Inviato STATS " + value + " sul topic topic_stats");

            } else if (tipo.equalsIgnoreCase("buy")) { 

                TopicPublisher publisher = topicSession.createPublisher(topic_tickets);


                synchronized (TICKET_LOCK) {
                    FileWriter writer = new FileWriter("tickets.txt", true); 
                    writer.write("[TICKET] - Concerto di " + value + "\t\t [STATO] Nel carrello.\n");
                    writer.close();
                }

                msg.setText(value);
                publisher.publish(msg);
                publisher.close();

                logger.log(Level.INFO, "Inviato BUY: " + value + " sul topic topic_tickets");
                logger.log(Level.INFO, "Scrittura su file avvenuta con successo");
                
            } 

            topicSession.close();
            

            } catch (JMSException e) {

                logger.log(Level.SEVERE, "Errore nella comunicazione JMS: ", e);

            } catch (IOException e) { 

                logger.log(Level.SEVERE, "Errore nella scrittura su file: ", e);
        }

    }

}
