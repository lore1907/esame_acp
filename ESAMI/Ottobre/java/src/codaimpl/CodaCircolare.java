package codaimpl;
import coda.*;

public class CodaCircolare implements Coda{
    
    private String data[]; 

    private int size; 
    private int elem; 

    private int tail; 
    private int head; 

    public CodaCircolare(int s){

        size = s;
        elem = tail = head = 0;
        data = new String[size]; 
    }


    public boolean full(){
        if (elem == size)
            return true; 
        return false;
    }

    public boolean empty(){
        if (elem == 0)
            return true; 
        return false;
    }

    public int getSize(){
        return size;
    }

    public void inserisci(String i) {
        data[tail%size] = i; 

        try{
            Thread.sleep(101+(int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        elem +=1;
        System.out.println("inserito" + i + " (tot = " + elem + " )"); 
        tail +=1; 

    }

    public String preleva(){ 

        String x = data[head%size]; 

        try{
            Thread.sleep(101+(int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        elem -=1;
        System.out.println("Prelevato" + x + " (tot = " + elem + " )"); 
        head +=1; 

        return x;
    }
}
