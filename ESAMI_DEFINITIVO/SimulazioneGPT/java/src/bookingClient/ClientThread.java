package bookingClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import services.IBooking;

public class ClientThread extends Thread{
    
    private static final int NUM_PRENOTAZIONI = 8;
    private static final Random rand = new Random();
    private static final String[] orari = {"19:00", "20:30", "22:00"};
    private Proxy proxy; 


    protected ClientThread(Proxy p){
        proxy = p;
    }


    private String genera_prenotazione(){

        String user = "user_" + rand.nextInt(100);
        int people = rand.nextInt(5)+1;
        String fascia_oraria = orari[rand.nextInt(orari.length)];

        String prenotazione = user + "-" + people + "-" + fascia_oraria;

        return prenotazione;
    }


    public void run(){

        for (int i=0; i<NUM_PRENOTAZIONI; i++){
            String prenotazione = genera_prenotazione();
            String user = prenotazione.split("-")[0];
            int people = Integer.parseInt(prenotazione.split("-")[1]);
            String orario = prenotazione.split("-")[2];

            System.out.println("Invio prenotazione..");
            proxy.book(user, people, orario);
            System.out.println("Inviata prenotazione con: USER- " + user + " PEOPLE- " + people + " ORARIO- " + orario);

        }
        
    }

}
