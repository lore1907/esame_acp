package client;
import java.util.logging.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import generated.*;
import io.grpc.*;

public class Client extends Thread{
    
    private final ManagerGrpc.ManagerBlockingStub blockingStub;
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private static final Random random = new Random();
    
    public Client(Channel channel) {
        blockingStub = ManagerGrpc.newBlockingStub(channel);
    }


    public void stopAll(String task_type){
        StopRequest request = StopRequest.newBuilder().setTaskType(task_type).build();
        try {
            blockingStub.stopAll(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC Failed: {0} ", e);
        }
    }

    public void deploy(long id, String name, String taskType){
        DeployRequest request = DeployRequest.newBuilder()
                                .setId(id)  
                                .setName(name)
                                .setTaskType(taskType)
                                .build();
        try {
            blockingStub.deploy(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC Failed: {0} ", e);
        }
    }

    public void run(){

        for (int i=0; i<4; i++){
            long id = random.nextInt(1000);
            String taskType; 
            if (random.nextBoolean()) {
                taskType = "gpu-bound";
            } else {
                taskType = "real-time";
            }

            String name;
            if (taskType.equals("gpu-bound")){ 
                name = "gpu-task-" + id;
            } else {
                name = "rt-control-" + id;
            }

            deploy(id, name, taskType);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }



    public static void main(String[] args) throws Exception{

        if (args.length <1) {
            logger.log(Level.WARNING, "sssss");
            return;
        }

        String target = "localhost:" +args[0];
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        
        try {
            
            Client[] threads = new Client[5];


            for (int i=0; i<5; i++){

                threads[i] = new Client(channel);
                threads[i].start()
            }
            
            for (int i=0; i<5; i++){

                threads[i].join();
            }

            Client stopClient = new Client(channel);
            stopClient.stopAll("gpu-bound");
            stopClient.stopAll("real-time");

        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}
