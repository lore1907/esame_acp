package temperature_server;
import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;
import services.*;
import coda.*;
import codaimpl.*;

public class TemperatureServer {
    
    private static final int SIZE = 50; 


    public static void main(String[] args) {

        Hashtable <String, String> prop = new Hashtable<String, String>(); 
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("queue.low", "low"); 
        prop.put("queue.mid", "mid"); 
        prop.put("queue.high", "high");
        

        try{ 
            Context ctx = new InitialContext(prop); 
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue severity_low = (Queue) ctx.lookup("low");
            Queue severity_mid = (Queue) ctx.lookup("mid");
            Queue severity_high = (Queue) ctx.lookup("high");
            QueueConnection qconn = qcf.createQueueConnection(); 
            QueueSession qSession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            

            TemperatureImpl impl = new TemperatureImpl(SIZE, qSession, severity_low, severity_mid, severity_high);
            TemperatureSkeleton skeleton = new TemperatureSkeleton(impl, Integer.valueOf(args[0]));
            skeleton.run_skeleton(); 
         
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    
}
