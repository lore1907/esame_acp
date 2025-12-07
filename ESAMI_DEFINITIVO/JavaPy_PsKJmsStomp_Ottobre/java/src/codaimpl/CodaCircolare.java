package codaimpl;

import coda.*;

public class CodaCircolare implements Coda{

    private int size;
    private String[] data;
    private int elem;
    private int head;
    private int tail;

    public CodaCircolare(int s){
        size = s;
        data = new String[size];
        head = elem = tail = 0;
    }


    public boolean empty(){ 
        if (elem==0)
            return true;
        return false;
    }

    public boolean full(){
        if (elem==size)
            return true;
        return false;
    }

    public int getSize(){
        return size;
    }

    public void inserisci(String i){

        data[tail%size] = i;

        try{
            Thread.sleep(101+(int)(Math.random()*100)); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tail += 1;
        elem += 1;

    }

    public String preleva(){

        String i = data[head%size];

        try{
            Thread.sleep(101+(int)(Math.random()*100)); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        head += 1;
        elem -= 1;

        System.out.println("Prelevato messaggio: "  + i + "dalla coda");

        return i;

    }

}
