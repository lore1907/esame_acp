package temperature;
import java.io.IOException;
import java.net.*;
import services.ITemperature;

public class Proxy implements ITemperature{
    
    private DatagramSocket s;

    protected Proxy(){
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void temp(float valore, int tipo) {
     
        String message = valore + "-" + tipo;
        try {

            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getLocalHost(), 8000);
            s.send(packet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }
        
}
