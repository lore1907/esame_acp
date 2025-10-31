package manager;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

import javax.naming.*;
import javax.jms.*;


import logger.LoggerUtil;
public class Manager {
    
    public static final Logger logger = Logger.getLogger(Manager.class.getName());

    public static void main(String[] args){

        LoggerUtil.setup();

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.request", "request");
        prop.put("topic.tickets", "tickets_shop");
        prop.put("topic.stats", "tickets_stats");


        try {

            Context jndiContext = new InitialContext(prop);
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");

            Topic request = (Topic) jndiContext.lookup("request");
            Topic t1 = (Topic) jndiContext.lookup("tickets");
            Topic t2 = (Topic) jndiContext.lookup("stats");

            TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
            topicConnection.start();
            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            TopicSubscriber topicSubscriber = topicSession.createSubscriber(request);

            ManagerListener listener = new ManagerListener(topicConnection, t1, t2);
            topicSubscriber.setMessageListener(listener);

            logger.info("----------- Manager avviato -----------");


            //Countdown latch per la chiusura delle risorse
            CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread( () -> {
                try{ 
                    logger.info("Shutdown in corso...");
                    topicSubscriber.close();
                    topicSession.close();
                    topicConnection.close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Errore in chiusura delle risorse", e);
                } finally {
                    latch.countDown();
                }
            }));
            latch.await();

        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Errore nel lookup degli administered objects: ", e);

        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Connessione fallita. Eccezione generata: ", e);

        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interruzione rilevata durante l'attesa. Chiusura anticipata del manager.", e);
            Thread.currentThread().interrupt(); //per resettare il flag di interruzione
        }


    }   

}
