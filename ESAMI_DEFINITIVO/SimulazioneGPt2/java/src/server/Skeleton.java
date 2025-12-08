package server;

import services.IBooking;

import java.io.IOException;
import java.net.*;

public class Skeleton implements IBooking{

    private IBooking delegato; 
    private static final int PORT = 8000;
 

    protected Skeleton(IBooking del){
        delegato = del;
    }

    public void run_skel(){

        try{

            ServerSocket server = new ServerSocket(PORT);
            
            while(true){
                Socket client = server.accept();
                ServerThread t = new ServerThread(client, delegato);
                t.start();
            }

        }catch (IOException  e){
            e.printStackTrace();
        } 

    }


    @Override
    public void book(String user, int people, String timeSlot) {
        delegato.book(user, people, timeSlot);
    }
    
    
}
