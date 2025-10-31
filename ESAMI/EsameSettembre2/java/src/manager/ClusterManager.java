package manager;
import static io.grpc.MethodDescriptor.newBuilder;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.*;

import io.grpc.*;
import logger.LoggerUtil;
import generated.*;


public class ClusterManager {
    
    private Server server; 
    private final static Logger logger = Logger.getLogger(ClusterManager.class.getName());

    private void start() throws IOException {

        int port = 0;
        ExecutorService executor = Executors.newFixedThreadPool(10); 
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .executor(executor)
                .addService(new ManagerImpl())
                .build()
                .start();
        logger.log(Level.INFO, "Server started, listening on " + server.getPort());
        
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.log(Level.WARNING, "Shutting down gRPC server since JVM is shutting down");
            try{
                ClusterManager.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        
        }));

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Server interrupted", e);
        }
    }

    private void stop()  throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws IOException {
        
        LoggerUtil.setup();
        final ClusterManager server = new ClusterManager();
        server.start();
    }
}



