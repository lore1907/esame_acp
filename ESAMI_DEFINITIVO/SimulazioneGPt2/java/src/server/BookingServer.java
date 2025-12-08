package server;
import javax.naming.*;

import services.BookingImpl;

import javax.jms.*;
import java.util.Hashtable;

public class BookingServer {

    public static void main(String[] args){

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put( "java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        prop.put("queue.booking", "booking");

        QueueConnection qconn = null;

        try {
            Context ctx = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue booking = (Queue) ctx.lookup("booking");
            qconn = qcf.createQueueConnection();

            BookingImpl impl = new BookingImpl(qconn, booking); 
            Skeleton skel = new Skeleton(impl); 
            skel.run_skel();

        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
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
