package manager;

import java.io.*;
import java.util.concurrent.*;

import io.grpc.*;

public class Manager {
    
    private Server server; 

    private void start() throws IOException{
        int port =0;
        ExecutorService exc = Executors.newFixedThreadPool(2); 
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                    .executor(exc)
                    .addService(new ManagerImpl())
                    .build()
                    .start(); 
        
        
        System.out.println("Server started listening on port: " + server.getPort());

        try { 
            server.awaitTermination();
        } catch (InterruptedException e){
            System.err.println("Server interrotto" + e); 
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final Manager server = new Manager(); 
        server.start();
    }

}