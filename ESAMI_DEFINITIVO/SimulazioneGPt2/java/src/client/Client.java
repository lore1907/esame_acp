package client;

public class Client {
    
    private static final int NUM_THREADS = 5;
    private static final int PORT = 8000;
    private static final String HOST = "localhost";
    private static final Proxy PROXY = new Proxy(PORT, HOST);
    
    public static void main(String[] args) {

        ClientThread[] threads = new ClientThread[NUM_THREADS]; 


        for(int i=0; i<NUM_THREADS; i++){
            threads[i] = new ClientThread(PROXY);
            threads[i].start();
        }

        try{
            for (ClientThread t: threads) t.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }


    }
}
