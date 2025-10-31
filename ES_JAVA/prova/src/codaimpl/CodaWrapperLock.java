package codaimpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import coda.Coda;
import coda.CodaWrapper;


public class CodaWrapperLock extends CodaWrapper{

    private Lock lock;
    private Condition full;
    private Condition empty;

    public CodaWrapperLock(Coda c) {
        
        super(c);
        
        lock = new ReentrantLock();
        full = lock.newCondition();
        empty = lock.newCondition();


    }


    public void deposita(int i) { 

        lock.lock();

        try {

            while (coda.full()) {

                try {

                    empty.await();

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

            }

            coda.deposita(i);

            full.signal();


        } finally {

            lock.unlock();
        }
    }

    public int preleva() { 


        int result = 0 ;
        
        lock.lock();
        try {

            while (coda.empty()) {

                try {

                    full.await();

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

            }

            result = coda.preleva();
            empty.signal();


        } finally {

            lock.unlock();
        }


        return result;

    }
}
