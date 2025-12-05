package client;
import generated.*;
import io.grpc.StatusRuntimeException;

import java.util.Random;

public class ClientThread extends Thread{

    private final ManagerGrpc.ManagerBlockingStub blockingStub;
    private static final String[] supported_types = {"gpu-bound", "real-time"};
    private static final Random random = new Random();
    
    public ClientThread(ManagerGrpc.ManagerBlockingStub bStub) {
        blockingStub = bStub;
    }

    private String[] genera_valori(String request){
         
        
        String tipo_scelto = supported_types[random.nextInt(2)];
        
        if (request.equalsIgnoreCase("stopAll")){
            String[] values = {tipo_scelto};
            return values;
        } else if (request.equalsIgnoreCase("deploy")){
            long _id = random.nextInt(100)+1;
            String name = _id + "_" + "task-" + tipo_scelto;
            String[] values = {String.valueOf(_id), name, tipo_scelto};
            return values;
        } else {
            System.out.println("Tipo non supportato"); 
            String[] error = {"ERR"};
            return error;
        }

    }

    @Override
    public void run(){

        try{
            for (int i=0; i<4; i++) {
                String[] params = genera_valori("deploy");
                System.out.println("[THREAD -" + this.getId() + "] Invio DEPLOY " + (i+1) + "/4: " + params[1] + " (" + params[2] + ")");
                
                DeployRequest request = DeployRequest.newBuilder()
                            .setId(Long.parseLong(params[0]))
                            .setName(params[1])
                            .setTipo(params[2])
                            .build();

                try {
                    blockingStub.deploy(request);
                } catch (StatusRuntimeException e){
                    System.out.println("RPC failed {0} " + e.getStatus());
                    return;
                }
                Thread.sleep(1000);
            }

            String tipo = genera_valori("stopAll")[0];
            System.out.println("[THREAD-" + this.getId() + "] Invio STOP_ALL per tipo " + tipo);

            StopRequest request = StopRequest.newBuilder().setTipo(tipo).build();

            try{
                blockingStub.stopAll(request); 
            } catch (StatusRuntimeException e){
                System.out.println("RPC failed: {0} " + e.getStatus());
                return;
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        
    }
    
}
