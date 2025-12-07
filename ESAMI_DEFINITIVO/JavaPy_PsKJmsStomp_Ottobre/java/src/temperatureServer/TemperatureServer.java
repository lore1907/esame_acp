package temperatureServer;
import javax.naming.*;
import javax.jms.*;
import javax.jms.Queue;

import services.TemperatureImpl;
import java.util.*;

public class TemperatureServer {


    private static final int QUEUE_SIZE = 50;
    private static final int PORT = 8000;

    public static void main (String[] args){


        Hashtable <String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "");
        prop.put("java.naming.provider.url", "");
        prop.put("queue.low", "low");
        prop.put("queue.mid", "mid");
        prop.put("queue.high", "high");


        try{
            Context ctx = new InitialContext(prop);

            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue low = (Queue) ctx.lookup("low");
            Queue mid = (Queue) ctx.lookup("mid");
            Queue high = (Queue) ctx.lookup("high");

            QueueConnection qconn = qcf.createQueueConnection();
            
            TemperatureImpl IMPL = new TemperatureImpl(QUEUE_SIZE, qconn, low, mid, high);
            Skeleton skel = new Skeleton(IMPL, PORT);
            skel.run_skel();


            try{
                while (true){
                    Thread.sleep(1000);
                }
            } catch (Exception e){
                System.out.println("Richiesta chiusura da tastiera con ctrl+c, chiudo la connection");
                qconn.close();
            }
            
        } catch (NamingException e){
            e.printStackTrace();
        } catch (JMSException e){
            e.printStackTrace();
        }
        
    }
  
}
