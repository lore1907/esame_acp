package server;

import services.IBooking;

import java.io.IOException;
import java.net.*;

public abstract class Skeleton implements IBooking{

    
    private int port; 

    protected Skeleton(int p){
        port = p;
    }


    public void run_skel(){

        try{
            ServerSocket server = new ServerSocket(port);
            
            while(true){
                Socket client = server.accept();

                ServerThread t = new ServerThread(client, this);
                t.start();

            }
            
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
}
