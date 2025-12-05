package temperature_server;
import services.*;

import java.io.IOException;
import java.net.*;


public class TemperatureSkeleton implements ITemperature{
    
    private ITemperature delegato; 

    private int port; 

    public TemperatureSkeleton(ITemperature delegato, int port){
        this.delegato = delegato;
        this.port = port;
    }

    public void run_skeleton() {

        try {
            DatagramSocket socket = new DatagramSocket(port); 
            System.out.println("[DISPATCHER SKELETON] Wating for connection...."); 

            while(true){
                byte[] buffer = new byte[65508]; 
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); 
                socket.receive(packet);

                WorkerThread t = new WorkerThread(socket, packet, this);
                t.start(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void temp(float valore, int tipo) {
        delegato.temp(valore, tipo);
    }
}
