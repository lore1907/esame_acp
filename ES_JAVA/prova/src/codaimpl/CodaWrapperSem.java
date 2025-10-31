package codaimpl;

import java.util.concurrent.Semaphore;

import coda.Coda;
import coda.CodaWrapper;

public class CodaWrapperSem extends CodaWrapper{


    private Semaphore postiDisponibili;
    private Semaphore elemDisponibili;

    public CodaWrapperSem(Coda c) {
        
        super(c); 

        postiDisponibili = new Semaphore( coda.getSize() );
        elemDisponibili = new Semaphore( 0 );

    }
    
    public void deposita ( int i ) {

        try {

            postiDisponibili.acquire();

            try { 

                synchronized(coda) { 
                    coda.deposita(i);(i);
                }

            } finally { 

                elemDisponibili.release();

            }

        } catch ( InterruptedException e ) { 

            e.printStackTrace();

        }
    }


    public int preleva() { 

        int x = 0; 


        try {

            elemDisponibili.acquire();

            try { 

                synchronized(coda) { 
                    coda.preleva();
                }

            } finally { 

                postiDisponibili.release();

            }

        } catch ( InterruptedException e ) { 

            e.printStackTrace();
            
        } 

        return x;

    }

}
