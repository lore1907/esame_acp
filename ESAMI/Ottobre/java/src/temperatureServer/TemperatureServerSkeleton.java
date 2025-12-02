package temperatureServer;
import java.io.*;
import java.net.*;

public class TemperatureServerSkeleton implements ITemperature{

    private ITemperature temperature; 

    private int port;

    public TemperatureServerSkeleton(ITemperature temperature, int port){
        this.temperature = temperature;
        this.port = port;
    }

    public void run_skel(){
        try{
            DatagramSocket socket = new DatagramSocket(port); 

            while(true){
                byte[] buffer = new byte[65508];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                ServerThread t = new ServerThread (socket, request, this);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void temp(float valore, int tipo){
        temperature.temp(valore, tipo);
    }
    
}
