package coda;

public interface Coda {

    public void deposita(int i);
    public int preleva(); 
    public boolean full();
    public boolean empty();
    public int getSize();

}