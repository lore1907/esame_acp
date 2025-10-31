package coda;

public abstract class CodaWrapper implements Coda{
    
    protected Coda coda;


    public CodaWrapper(Coda c){
        coda = c;
    }


    public boolean full(){

        return coda.full();

    }

    public boolean empty() {

        return coda.empty();

    }


    public int getSize() {

        return coda.getSize();

    }
    
}
