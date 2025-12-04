package server;
import java.net.*;


import service.*;
import java.io.*;

public class ServerThread extends Thread{
    
    private Socket sock; 
    private ILogging skeleton;

    public ServerThread(Socket s, ILogging skel){
        sock = s;
        skeleton = skel;
    }

    public void run(){
        
        try {
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

            String messaggioLog = dataIn.readUTF(); 
            int tipo = dataIn.readInt(); 

            skeleton.log(messaggioLog, tipo);
            dataOut.writeUTF("ACK");

            dataOut.flush()

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally { 
            if (sock != null)
                sock.close();
        }

    }
}
