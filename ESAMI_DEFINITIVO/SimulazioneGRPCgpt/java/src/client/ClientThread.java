package client;

import java.util.Random;

public class ClientThread extends Thread{
    
    private Proxy proxy; 
    private static final int NUM_REQ = 5;
    private static final String[] timeSlots = {"19:00", "20:30", "22:00"};
    private static final Random rand = new Random();

    protected ClientThread(Proxy p){
        proxy = p;
    }


    public void run(){

        try{

            for (int i=0; i<NUM_REQ; i++){

                String user = "user_" + rand.nextInt(100);
                int people = rand.nextInt(6)+1;
                String timeSlot = timeSlots[rand.nextInt(timeSlots.length)];
                System.out.println("Invio richiesta prenotazion: UTENTE" + user + ", PERSONE: " + people +", Orario di Arrivo: " + timeSlot);
                proxy.book(user, people, timeSlot);
                Thread.sleep(1000);
            }

        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
