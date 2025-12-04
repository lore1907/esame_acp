package server;

import javax.naming.*;

import java.util.Hashtable;

import javax.jms.*;


public class LoggingServer {
    
    public static void main(String[] args) {

        

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("queue.info", "info");
        prop.put("queue.error", "error"); 

        try{ 
            int port = 8000;
            Context ctx = new InitialContext(prop); 
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue queue_info = (Queue) ctx.lookup("info");
            Queue queue_error = (Queue) ctx.lookup("error"); 

            QueueConnection qc = qcf.createQueueConnection();
            LoggingServiceImpl impl = new LoggingServiceImpl(port, qc, queue_error, queue_info);

        } catch(NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    
    
    }
}
