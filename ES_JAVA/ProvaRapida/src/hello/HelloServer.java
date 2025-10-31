package hello;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;

public class HelloServer {
    
    private Server server; 

    private void start() throws IOException {

        int port = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
            .executor(executor)
            .addService(new GreeterImpl())
            .build()
            .start();

        System.out.println("[HelloworldServer] Server started, listening on : " + server.getPort());

        
        try {
            System.out.println("[HelloWorldServer] Await for termination...");
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) throws IOException, InterruptedException {

        final HelloServer hello_server = new HelloServer();
        hello_server.start();
    }
    
}
