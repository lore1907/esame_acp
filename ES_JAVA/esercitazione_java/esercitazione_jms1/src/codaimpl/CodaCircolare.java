package codaimpl;

import coda.Coda;

public class CodaCircolare implements Coda{


    private int size; 

    private int head; 
    private int tail; 

    private int data[];
    private int elem;

    public CodaCircolare (int s){
        
        size = s;
        elem = 0;
        head = tail = 0; 
        data = new int[size];

    }

    public boolean full() {

        if (elem == size){ 
            return true;
        }

        return false; 

    }

    public boolean empty() {

        if (elem == 0){
            return true;
        }

        return false;

    }

    public int getSize() {

        return size;

    }

    public void deposita(int i) {

        data[tail%size] = i;

        try {

            Thread.sleep(101 + (int)Math.random()*100);

        } catch (InterruptedException e) { 

            e.printStackTrace();

        }

        elem += 1;
        System.out.println("Depositato valore: " + i + " ( tot elem = " + elem + " )");
        tail +=1;

    }

    public int preleva() {

        int x = data[head%size];

        try {

            Thread.sleep(101 + (int)Math.random()*100);

        } catch (InterruptedException e) { 

            e.printStackTrace();

        }

        elem -= 1;
        System.out.println("Depositato valore: " + i + " ( tot elem = " + elem + " )");
        head -= 1;

        return x;

    }

    
}
