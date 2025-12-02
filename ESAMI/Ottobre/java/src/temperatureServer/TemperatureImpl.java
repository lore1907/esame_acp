package temperatureServer;
import coda.CodaWrapper;
import codaimpl.CodaCircolare; 
import codaimpl.CodaWrapperLock; 

public class TemperatureImpl implements ITemperature{
    
    private CodaWrapper codaWrapper; 

    public TemperatureImpl(int size){
        CodaCircolare coda = new CodaCircolare(size);
        codaWrapper = new CodaWrapperLock(coda);
    }
    
    private class TemperatureWorker implements Runnable {
        private float valore;
        private int tipo;

        public TemperatureWorker(float valore, int tipo){
            this.valore = valore;
            this.tipo = tipo;
        }

        @Override
        public void run(){
            String severity = "";
            switch(tipo) {
                case 0: severity = "LOW"; break;
                case 1: severity = "MID"; break;
                case 2: severity = "HIGH"; break;
                default: severity = "UNKNOWN";
            }

            String data = valore + "-" + severity;
            codaWrapper.inserisci(data);
            System.out.println("[SERVER-IMPL] Prodotto: " + data);
        }
    }

    @Override
    public void temp(float valore, int tipo){
       TemperatureWorker worker = new TemperatureWorker(valore, tipo);
       Thread t = new Thread(worker);
       t.start();
    }
}
