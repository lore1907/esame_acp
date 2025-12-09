package server;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.*;
import javax.naming.*;
import javax.naming.Context;

import io.grpc.*;


public class BookingServer {
    
    private static final int port = 8000;
    private static final int grpc_port = 50051;
    
   
    public static void main(String[] args){

        Hashtable<String, String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        prop.put("queue.booking", "booking");

        QueueConnection qconn = null;

        try {
            Context ctx = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue booking = (Queue) ctx.lookup("booking");
            qconn = qcf.createQueueConnection();

            ExecutorService executor = Executors.newFixedThreadPool(10);
            ManagerImpl manager = new ManagerImpl();
        
            Server server = Grpc.newServerBuilderForPort(grpc_port, InsecureServerCredentials.create())
                        .executor(executor)
                        .addService(manager)
                        .build()
                        .start();

            ServiceImpl impl = new ServiceImpl(port, qconn, booking, manager);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {

                try{
                    server.shutdown();
                } catch( Exception e ){
                    e.printStackTrace();
                }

            }));

            impl.run_skel();

            
            try{
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            
        } catch (NamingException e){
            e.printStackTrace();
        } catch (JMSException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }
}
