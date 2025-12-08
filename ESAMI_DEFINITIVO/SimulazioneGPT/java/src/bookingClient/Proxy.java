package bookingClient;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;

import services.IBooking;

public class Proxy implements IBooking{

    private int port;
    private String host;

    protected Proxy(int p, String h){
        host=h;
        port=p;
    }

    @Override
    public void book(String user, int people, String timeSlot){  
        
        Socket client = null;
        DataOutputStream dataOut = null;

        try{

            client = new Socket(host, port);
            dataOut = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            
            dataOut.writeUTF(user);
            dataOut.writeInt(people);
            dataOut.writeUTF(timeSlot);

            dataOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            try{
                if (dataOut != null) dataOut.close();
                if (client != null) client.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }


    }
    
    
}
