package temperature;

import java.util.Random;

public class ClientThread extends Thread{

    private Proxy proxy;
    private static final int NUM_MISURAZIONI = 10;
    private static final Random rand = new Random();

    protected ClientThread(){
        proxy = new Proxy();
    }

    public void run(){

        for (int i=0; i<NUM_MISURAZIONI; i++) {
            int tipo = rand.nextInt(3);
            float valore = rand.nextFloat()*30f;

            proxy.temp(valore, tipo);
        }    
    }
}
