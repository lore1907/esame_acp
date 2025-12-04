package client;
import java.util.Hashtable;

import javax.jms.*;
import javax.naming.*;


import java.util.Random;

public class Client {
    
    private TopicSession session; 

    private static final String[] type = {"buy", "stats"};  
    private static final String[] value = {"Jovanotti", "Ligabue", "Negramaro"};
    private static final int NUM_REQS = 20;
    private static final Random rand = new Random();

    public Client(TopicSession session){
        this.session = session;
    }


    private MapMessage genera_Richiesta() throws JMSException{ 

        int indice_tipo = rand.nextInt(type.length); 
        int indice_valore = rand.nextInt(value.length); 

        String tipo_scelto = type[indice_tipo];
        String valore_scelto = "";

        if (tipo_scelto.equalsIgnoreCase("stats")){
            valore_scelto = "Sold";
        } else if (tipo_scelto.equalsIgnoreCase("buy")){
            valore_scelto = value[indice_valore];
        }

        MapMessage msg = session.createMapMessage();

        msg.setString("type", tipo_scelto); 
        msg.setString("value", valore_scelto);

        return msg;
    }

    public static void main(String[] args){

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.request", "request"); 

        try{ 
            Context ctx = new InitialContext(prop); 
            TopicConnectionFactory tcf = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory"); 
        
            Topic request = (Topic) ctx.lookup("request");

            TopicConnection tconn = tcf.createTopicConnection();
            tconn.start();
            System.out.println("Manager avviato. In attesa di richieste...."); 
            TopicSession ts = tconn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicPublisher tpub = ts.createPublisher(request);
            Client client = new Client(ts);

            for (int i=0; i<NUM_REQS; i++){
                MapMessage msg = client.genera_Richiesta();
                tpub.publish(msg);
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
