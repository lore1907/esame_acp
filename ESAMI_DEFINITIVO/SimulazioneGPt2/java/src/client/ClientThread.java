package client;

import java.util.Random;

public class ClientThread extends Thread{
    
    private static final int NUM_PRENOTAZIONI = 8;
    private static final String[] timeSlots = {"19:00", "20:30", "22:00"};
    private static final Random random = new Random();
    private Proxy proxy;

    protected ClientThread(Proxy proxy){
        this.proxy = proxy;
    }


    public void run(){

        String timeslot; 
        String user = "user_";
        int people; 

        for (int i=0; i<NUM_PRENOTAZIONI; i++){
            timeslot = timeSlots[random.nextInt(timeSlots.length)]; 
            String gen_user = user + random.nextInt(100);
            people = random.nextInt(6)+1;

            proxy.book(gen_user, people, timeslot);
        }

    }
}
