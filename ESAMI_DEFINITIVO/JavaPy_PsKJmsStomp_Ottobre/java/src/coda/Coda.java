package coda;

public interface Coda {

    public void inserisci(String misura_temperatura);
    public String preleva();
    public boolean empty();
    public boolean full(); 
    public int getSize();
    
} 