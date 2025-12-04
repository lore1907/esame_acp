package server;
import service.*;

import java.io.IOException;
import java.net.*;


public abstract class ServerSkeleton implements ILogging{
   
    private int port; 

    public ServerSkeleton(int p){
        port = p;
    }

    public void runSkeleton(){

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("[SERVER SKELETON] Waiting for connections....");

            while(true){ 
                Socket s = server.accept(); 
                ServerThread t = new ServerThread(s, this); 
                t.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
