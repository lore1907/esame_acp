package server;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import services.IBooking;

public class ServerThread extends Thread{
    
    private Socket c; 
    private IBooking del;


    protected ServerThread(Socket c, IBooking delegato){
        this.c = c;
        this.del = delegato;
    }


    public void run(){

        DataInputStream dataIn = null;
        try{
            dataIn = new DataInputStream(new BufferedInputStream(c.getInputStream())); 
            String user; 
            int people; 
            String timeSlot;

            user = dataIn.readUTF();
            people = dataIn.readInt();
            timeSlot = dataIn.readUTF(); 

            del.book(user, people, timeSlot);

        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(dataIn !=null){dataIn.close();}
                c.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}   
