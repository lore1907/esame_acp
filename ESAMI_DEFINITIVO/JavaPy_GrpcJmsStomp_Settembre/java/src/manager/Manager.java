package manager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import generated.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import javax.jms.*;
import javax.jms.Queue;
import javax.naming.*;
import javax.naming.Context;

import java.util.*;


public class Manager {
    
    private Server server; 
    private QueueConnection qconn; 
    private Queue gpu;
    private Queue rt;

    // aggiunto costruttore per passare ai metodi la connection e le code cosi che le passa al thread 
    // che puo creare sessione e sender alle varie code
    
    public Manager(QueueConnection qc, Queue qgpu, Queue qrt){
        qconn = qc;
        gpu = qgpu;
        rt = qrt;
    }

    private void start() throws IOException{
        int port = 0;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                                                .executor(executor)
                                                .addService(new ManagerImpl())
                                                .build()
                                                .start();

        System.out.println("Server started, listening on: " + server.getPort());   

        
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {

            System.out.println("Shutdown hook in esecuzione..."); 
            server.shutdown(); 

        }));

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
                                                      
    }

    public class ManagerImpl extends ManagerGrpc.ManagerImplBase {

        @Override 
        public void deploy(DeployRequest req, StreamObserver<Empty> responsObserver){
            System.out.println("[SERVER JAVA GRPC] Invoked Deploy " + req.getTipo());
            Empty empty = Empty.newBuilder().build(); 
            responsObserver.onNext(empty);
            responsObserver.onCompleted();

            String message = "deploy" + "_" + req.getId() + "_" + req.getName();
            ManagerThread t = new ManagerThread(qconn, gpu, rt, req, message); 
            t.start(); 

        }

        @Override 
        public void stopAll(StopRequest req, StreamObserver<Empty> responsObserver){
            System.out.println("[SERVER JAVA GRPC] Invoked StopAll " + req.getTipo());
            Empty empty = Empty.newBuilder().build(); 
            responsObserver.onNext(empty);
            responsObserver.onCompleted();

            ManagerThread t = new ManagerThread(qconn, gpu, rt, req); 
            t.start();

        }
    }
    


    public static void main(String[] args) throws IOException {
        
        Hashtable<String,String> prop = new Hashtable<String, String>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");

        prop.put("queue.gpu", "gpu");
        prop.put("queue.rt", "rt"); 

        
        try{

            Context ctx = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue gpu = (Queue) ctx.lookup("gpu");
            Queue rt = (Queue) ctx.lookup("rt");

            QueueConnection qconn = qcf.createQueueConnection();
            final Manager server = new Manager(qconn, gpu, rt);
            server.start(); 
            

        } catch (NamingException e){ 
            e.printStackTrace();
        } catch (JMSException q) {
            q.printStackTrace();
        }
       
    }

}
