package temperatureServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.StringTokenizer;

import services.ITemperature;

public class Skeleton implements ITemperature{

    private ITemperature delegato;
    private int port;
    private static final String token = "-";

    protected Skeleton(ITemperature delegato, int port){
        this.delegato = delegato;
        this.port = port;
    }


    public void run_skel(){

        StringTokenizer tokenizer;
        DatagramSocket s = null;
        try {
            s = new DatagramSocket(port);
            
            byte[] buf = new byte[65508];

            while(true){
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
                s.receive(packet);

                String received_packet = new String(packet.getData(), 0, packet.getLength()); 
                tokenizer = new StringTokenizer(received_packet); 

                float valore = Float.parseFloat(tokenizer.nextToken(token));
                int tipo = Integer.parseInt(tokenizer.nextToken(token));

                delegato.temp(valore, tipo);
            } 

            
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s!=null){
                s.close();
            }
        }
    }

    public void temp(float valore, int tipo){
        delegato.temp(valore, tipo);
    }

}
