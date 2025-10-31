package codaimpl;

import coda.*;
import coda.CodaWrapper;

public class CodaWrapperSynchr extends CodaWrapper{



    public CodaWrapperSynchr (Coda c) { 

        super(c);

    }


    public int preleva() {

        int x = 0;
        
        synchronized(coda) {

            while (coda.empty()){
                
                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            x = coda.preleva(); 
            coda.notifyAll();
        }

        return x;

    }


    public void deposita(int i) {

        synchronized(coda) {

            while (coda.full()){

                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            
            coda.deposita(i);
            coda.notifyAll();
        }
    }



}
