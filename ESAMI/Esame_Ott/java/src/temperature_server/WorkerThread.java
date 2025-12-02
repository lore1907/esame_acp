package temperature_server;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

import services.ITemperature; 

public class WorkerThread extends Thread{
    
    private DatagramSocket socket; 
    private DatagramPacket packet; 
    private ITemperature temperature; 

    private static final String SEPARATORE = "-"; 


    public WorkerThread(DatagramSocket s, DatagramPacket p, ITemperature t){ 
        socket = s;
        packet = p; 
        temperature = t; 
    }

    public void run() {
        System.out.println("[DISPATCHER THREAD] run thread..."); 

        try {
            String message = new String(packet.getData(), 0, packet.getLength()); 
            StringTokenizer tokenizer = new StringTokenizer(message, SEPARATORE); 
            
            if (tokenizer.countTokens() != 2) {
                System.out.println("[WORKER THREAD] Formato non valido, aspettato: ' valore-tipo ', ricevuto: " +message);
                return;
            }

            float valore = Float.valueOf(tokenizer.nextToken()); 
            int tipo = Integer.valueOf(tokenizer.nextToken()); 
            temperature.temp(valore, tipo);
        } catch (NumberFormatException e) {
            System.out.println("[WORKER THREAD] Errore nel parsing. ");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
