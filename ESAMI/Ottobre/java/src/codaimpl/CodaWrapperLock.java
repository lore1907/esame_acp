package codaimpl;

import java.util.concurrent.locks.*;

import coda.*;

public class CodaWrapperLock extends CodaWrapper{
    
    private Lock lock; 
    private Condition empty; 
    private Condition full; 

    public CodaWrapperLock(Coda c){
        super (c);

        lock = new ReentrantLock();

        empty = lock.newCondition(); 
        full = lock.newCondition();
    }

    public void inserisci(String i){

        lock.lock(); 

        try{
            while(coda.full()){
                try{
                    empty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            coda.inserisci(i);
            full.signal();
        } finally {
            lock.unlock();
        }
    }

    public String preleva(){

        lock.lock();
        String stringa = "";
        try{
            while(coda.empty()){
                try{
                    full.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            stringa = coda.preleva();
            empty.signal();
        } finally {
            lock.unlock();
        }

        return stringa; 
    }

}
