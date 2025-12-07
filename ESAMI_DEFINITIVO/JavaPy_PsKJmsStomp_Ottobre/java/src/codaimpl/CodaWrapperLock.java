package codaimpl;
import coda.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CodaWrapperLock extends CodaWrapper{

    private Lock lock;
    private Condition prod_cv;
    private Condition cons_cv;

    public CodaWrapperLock(Coda c){

        super(c);
        lock = new ReentrantLock(); 
        prod_cv = lock.newCondition(); 
        cons_cv = lock.newCondition();

    }

    @Override
    public void inserisci(String misura_temperatura) {
        
        lock.lock();

        try{

            while(coda.full()){
                try {
                    prod_cv.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            coda.inserisci(misura_temperatura);
            cons_cv.notify();

        } finally {
            lock.unlock();
        }
    }

    @Override
    public String preleva() {
        
        String i; 

        lock.lock();

        try{

            while(coda.empty()){
                try {
                    cons_cv.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            i = coda.preleva();
            prod_cv.signal();

        } finally { 
            lock.unlock();
        }


        return i;
    }


    
}
