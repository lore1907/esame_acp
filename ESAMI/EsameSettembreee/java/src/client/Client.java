package client;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import generated.*; 
import io.grpc.*;

public class Client extends Thread{
    
    private final ManagerGrpc.ManagerBlockingStub bstub;
    private static final Random random = new Random();

    public Client (Channel ch) { 
        bstub = ManagerGrpc.newBlockingStub(ch);
    }

    public void Deploy (long id, String taskType, String name) {
        System.out.println("Invio Deploy request, TASKTYPE : " + taskType + "/t name: " + name); 

        DeployReq req = DeployReq.newBuilder()
                                .setTask(taskType)
                                .setId(id)
                                .setName(name)
                                .build();
        
        try {
            bstub.deploy(req); 
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0}" + e.getStatus());
        }
    }

    public void StopAll (String taskType) {
        System.out.println("Invio Stop request, TASKTYPE : " + taskType);

        StopReq req = StopReq.newBuilder().setTask(taskType).build();

        try{
            bstub.stopAll(req); 
        }catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0}" + e.getStatus()); 
        }
    }

    public void run(){

        String taskType; 

        for (int i=0; i<4; i++){
            long id = random.nextInt(1000);
            taskType = random.nextBoolean() ? "gpu-bound" : "real-time"; 
            String name = "";

            if (taskType.equalsIgnoreCase("gpu-bound")) {
                name = "gpu-task-" + id;
            } else if (taskType.equalsIgnoreCase("real-time")){
                name = "rt-control-" + id;
            }

            Deploy(id, taskType, name);

            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }

        taskType = random.nextBoolean() ? "gpu-bound" : "real-time"; 
        StopAll(taskType);
    }

    

    public static void main(String args[]) throws Exception {

        if (args.length <1) {
            System.out.println("Inserire numero di porta...");
            return;
        }

        String target = "localhost:" + args[0]; 
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        
        try {
            Client[] clients = new Client[5];

            for(int i=0; i<5; i++){
                clients[i] = new Client(channel);
                clients[i].start();
            }

            for(int i=0; i<5; i++){
                clients[i].join();
            }

        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}
