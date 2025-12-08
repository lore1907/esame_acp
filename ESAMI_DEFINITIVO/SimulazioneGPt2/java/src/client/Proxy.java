package client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import services.IBooking;

public class Proxy implements IBooking{


    private int port; 
    private String host;
    

    public Proxy(int port, String host){
        this.port = port;
        this.host = host;
    }


    @Override
    public void book(String user, int people, String timeSlot) {
       
        try {
            Socket s = new Socket(host, port);

            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));

            dataOut.writeUTF(user);
            dataOut.writeInt(people);
            dataOut.writeUTF(timeSlot);

            dataOut.flush();

            s.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
