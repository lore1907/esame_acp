package codaimpl;
import coda.*; // per importare un package 


public class CodaCircolare implements Coda{


    private int head;
    private int tail; 

    private int data[]; 

    private int size; 
    private int elem;
    

    public CodaCircolare(int s){ 

        size = s;

    }


    public int getSize() {

        return size;

    }


    public boolean full() {

        if (elem == size) { 
            return true;
        }

        return false; 
    }

    public boolean empty() {

        if (elem == 0) { 
            return true;
        }

        return false; 
    }

    public void deposita(int i) { 

        data[tail%size] = i; 

        try { 

            Thread.sleep(101 +  (int)Math.random()*100);
            
        } catch (InterruptedException e) { 

            e.printStackTrace();

        }

        elem += 1;
        System.out.println("Depositato il valore : " + i + " ( tot elem = " + elem + " )")
        tail += 1 ;

    }


    public int preleva() {
        
        int result = data [head%size];

        try { 

            Thread.sleep(101 +  (int)Math.random()*100);
            
        } catch (InterruptedException e) { 

            e.printStackTrace();

        }

        elem -= 1;
        System.out.println("Prelevato il valore : " + result + " ( tot elem = " + elem + " )")
        head -= 1 ;

        return result; 
    }



}
