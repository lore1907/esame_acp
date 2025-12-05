package temperature;
import java.io.IOException;
import java.net.*;
import java.rmi.UnknownHostException;


import services.ITemperature;

public class TemperatureProxy implements ITemperature{
    
    private String addr; 
    private int port; 
    private DatagramSocket socket;

    public TemperatureProxy(String a, int p){
        addr = new String(a); 
        port = p; 

        try {
            socket = new DatagramSocket();
        } catch (SocketException e){
            e.printStackTrace();
        }
    }

    @Override
    public void temp(float valore, int tipo){

        String message = new String(valore + "-" + tipo); 

        try{
            DatagramPacket request = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(addr), port);
            System.out.println("[DISPATCHER PROXY] Sending : " + message); 
            socket.send(request);
        
        } catch (UnknownHostException e){ 
            e.printStackTrace();
        } catch (IOException i){
            i.printStackTrace();
        }
    }
    
}
