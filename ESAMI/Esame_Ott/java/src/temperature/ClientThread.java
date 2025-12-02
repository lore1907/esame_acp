package temperature; 

import java.util.Random;



public class ClientThread extends Thread{
   
    private final TemperatureProxy proxy;
    private static final int NUM_REQS = 10; 

    public ClientThread(String ip_address, int port){ 
        proxy = new TemperatureProxy(ip_address, port);
    }

    @Override
    public void run(){
        Random random = new Random();
        for (int i=0; i<NUM_REQS; i++){ 
            float valore = random.nextFloat() * 30; 
            int tipo = random.nextInt(3);

            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            proxy.temp(valore, tipo); 
        }   
    }
}
