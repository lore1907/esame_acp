package server;

import services.IBooking;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class ServerThread extends Thread{
 
    private Socket s;
    private IBooking bookingManager;

    protected ServerThread(Socket s, IBooking bookingManager){
        this.s = s;
        this.bookingManager = bookingManager;
    }

    public void run(){

        try{
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(s.getInputStream()));

            String user = dataIn.readUTF();
            int people = dataIn.readInt();
            String timeSlot = dataIn.readUTF();

            String prenotazione = user + "-" + people + "-" + timeSlot;

            System.out.println("[SKELETON] Ricevuta prenotazione: " + prenotazione);
            bookingManager.book(user, people, timeSlot);

            dataIn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("SKELETON- servita richiesta prenotazione. Chiudo socket con client....");
            try {
                s.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
