package temperature;

public class TemperatureClient {

    private static final int NUM_THREADS = 5;

    public static void main(String[] args) {
        String ip_address = "localhost"; 
        int port = Integer.valueOf(args[0]); 

        ClientThread threads[] = new ClientThread[NUM_THREADS]; 

        for(int i=0; i<NUM_THREADS; i++){
            threads[i] = new ClientThread(ip_address, port);
            threads[i].start();
        }

        for (int i=0; i<NUM_THREADS; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
