package client;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import generated.*;

import io.grpc.*;

public class Client {
    
    private final ManagerGrpc.ManagerBlockingStub blockingStub;
    private static final int NUM_THREADS = 5;

    public Client(Channel ch){
        blockingStub = ManagerGrpc.newBlockingStub(ch);
    }
    
    //aggiunto un getter perche essendo statico e l'unico modo di accedere nel main e passarlo al clientThread
    public ManagerGrpc.ManagerBlockingStub getStub(){
        return blockingStub;
    }

    public static void main(String[] args) throws InterruptedException{

        if (args.length != 1){
            System.out.println("Inserire numero di porta");
            System.exit(1);
        }

        String target = "localhost:" + args[0]; 
        

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();

        try {
            Client client = new Client(channel);
            ClientThread[] threads = new ClientThread[NUM_THREADS]; 

            for (int i=0; i<NUM_THREADS; i++){

                threads[i] = new ClientThread(client.getStub()); 
                threads[i].start();
            }

            //for each
            for (ClientThread t: threads) t.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

    }
}
