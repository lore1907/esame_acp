package bookingServer;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.*;

import services.Bookingimpl;


public class BookingServer {

    
    public static void main(String[] args) {
        
        Hashtable<String,String> prop = new Hashtable<String, String>();

        prop.put( "java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("queue.booking", "booking"); 

        QueueConnection qconn = null;
        
        try{
            Context ctx = new InitialContext(prop); 
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue booking_queue = (Queue) ctx.lookup("booking");
            qconn = qcf.createQueueConnection();
        
            Bookingimpl impl = new Bookingimpl(qconn, booking_queue);
            Skeleton skel = new Skeleton(impl, 8000);
            skel.run_skel();

        } catch(NamingException e) {
            e.printStackTrace();
        } catch(JMSException e) {
            e.printStackTrace();
        } finally {
            if(qconn!=null){
                try {
                    qconn.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
