package client;

import services.IBooking;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Proxy implements IBooking{
    
    private String host;
    private int port;

    public Proxy(String host, int port){
        this.host = host;
        this.port = port;

    }

    @Override
    public void book(String user, int people, String timeSlot) {
        
        Socket s = null;
        try {
            s = new Socket(host, port);
            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream())); 
            System.out.println("[PROXY] Connesso al server, invio dati prenotazione....");
            dataOut.writeUTF(user);
            dataOut.writeInt(people);
            dataOut.writeUTF(timeSlot);

            dataOut.flush();
            System.out.println("[PROXY] Prenotazione: " + user + ", n_pers: " + people + ", orario: " + timeSlot + ". Inviata al server");
            dataOut.close();

        } catch (IOException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if(s != null){
                try {
                    s.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
       
    }



}
