package temperatureServer;

import coda.*;
import codaimpl.*;

public class TemperatureServer {
    
    private static final int QUEUE_SIZE = 5;
    
    public static void main(String[] args) {
        
        int port = 9000;

        System.out.println("[TEMPERATURE SERVER] Server Started...");

        TemperatureImpl impl = new TemperatureImpl(QUEUE_SIZE);
        CodaCircolare codaCircolare = new CodaCircolare(QUEUE_SIZE);
        CodaWrapper coda = new CodaWrapperLock(codaCircolare);

        Consumer jmsConsumer = new Consumer(coda);
        TemperatureServerSkeleton skel = new TemperatureServerSkeleton(impl, port); 
        skel.run_skel(); 

        
    }

    public static class Consumer extends Thread {

        private CodaWrapper coda; 
        private String url = "tcp://localhost:61616"; 

        public Consumer(CodaWrapper coda) {
            this.coda = coda; 
        }

        @Override 
        public void run(){
            Connection connection = null; 
        }
    }
}
