package bookingClient;

import java.util.Random;

public class Client {
    
    private static final int NUM_THREADS = 5;
    private static final int PORT = 8000;
    private static final String HOST = "localhost";
    public static void main(String[] args) {
        
        ClientThread[] threads = new ClientThread[NUM_THREADS];
        Proxy proxy = new Proxy(PORT, HOST);

        for (int i=0; i<NUM_THREADS; i++){
            threads[i] = new ClientThread(proxy);
            threads[i].start();
        }

        for(ClientThread t :threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
