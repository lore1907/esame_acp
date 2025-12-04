package clientService;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import server.ServerThread;
import service.*;

public class Proxy implements ILogging{

    private int port; 
    private String host;
    
    public Proxy(String h, int p){
        host = h;
        port = p;
    }

    @Override
    public void log(String messaggioLog, int tipo) {
        try {
            Socket client = new Socket(host, port);
            
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            System.out.println("[PROXY] Inviando richiesta di log: " + messaggioLog + " con tipo: " + tipo); 
            
            dataOut.writeUTF(messaggioLog); 
            dataOut.writeInt(tipo);

            String response = dataIn.readUTF(); 
            if (response.equalsIgnoreCase("ACK")){
                System.out.println("Richiesta di log soddisfatta.");
            }
        } catch (UnknownHostException e){
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



   


    
}
