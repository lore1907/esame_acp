package client;

import java.util.Hashtable;
import java.util.Random;
import java.util.logging.*;

import javax.naming.*;
import javax.jms.*;

import logger.LoggerUtil;


public class Client {
    
    private static final Logger logger = Logger.getLogger(Client.class.getName());

        //private static String[] artista = {"Ligabue", "Jovanotti", "Negramaro"};
        private static String[] artista = {"Ligabue", "Jovanotti", "Negramaro"};
    

    public static MapMessage generaRichiesta(String tipo, TopicSession s) throws JMSException{

        MapMessage msg = s.createMapMessage();
        Random generatore = new Random();
        int indiceCasuale = generatore.nextInt(artista.length);
        
        if (tipo.equalsIgnoreCase("buy")){
            msg.setString("type", tipo);
            msg.setString("value", artista[indiceCasuale]);
        } else if (tipo.equalsIgnoreCase("stats")) {
            msg.setString("type", tipo);
            msg.setString("value", "Sold");
        } else {
            logger.log(Level.WARNING, "Errore, richiesta non accettata");
        }

        return msg;
    }


    public static void main(String[] args){

        LoggerUtil.setup();
        if (args.length != 1) {
            logger.log(Level.WARNING, "Si prega di specificare il tipo di richiesta. \n \tScegliere tra: [buy, stats]");
        }

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.request", "request");
        

        try {
            
            Context jndiContext = new InitialContext(prop);
            
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            Topic request =  (Topic) jndiContext.lookup("request");
            
            TopicConnection tc = tcf.createTopicConnection();
            TopicSession ts = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            TopicPublisher pub = ts.createPublisher(request);
            
            if (args[0].equalsIgnoreCase("stats")){
                MapMessage stats_message = generaRichiesta(args[0], ts);
                pub.publish(stats_message);
            }
            else {
                for (int i=0; i<19; i++) {
                    Thread.sleep(2000);
                    MapMessage msg_to_send = generaRichiesta(args[0], ts);
                    pub.publish(msg_to_send);
                } 
            }

            pub.close();
            ts.close();
            tc.close();

        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Errore nel lookup degli administered objects: ", e);

        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Connessione fallita. Eccezione generata: ", e);

        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interruzione rilevata durante l'attesa. Chiusura anticipata del manager.", e);
            //Thread.currentThread().interrupt(); //per resettare il flag di interruzione
        } 
 

    }

}
