package temperature;

public class TemperatureCient {
    
    private static final int NUM_THREADS = 5;

    public static void main(String[] args){

        Thread[] threads = new Thread[5];

        try {
            for(int i=0; i<NUM_THREADS; i++){
                threads[i] = new ClientThread();
                threads[i].start();
            }

            for(Thread t: threads) t.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
