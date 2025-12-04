package clientService;

import java.util.Random;

public class Service {
    
    private static final String[] messaggi_log = {"success", "checking"};
    private static final String[] errors_log = {"fatal", "exception"};
    private static final Random rand = new Random();
    private static final int NUM_REQS = 10;
    private static final String host = "localhost"; 
    private static final int port = 8000;

    private static final Proxy proxy = new Proxy(host, port);

    public static void main(String[] args) {
        String messaggioLog = "";
        String host = "localhost"; 
        int port = 8000;
        
        for (int i=0; i< NUM_REQS; i++){
            
            int tipo = rand.nextInt(3); 
            int indice_messaggio = rand.nextInt(3);
            if (tipo < 2){
                messaggioLog = messaggi_log[indice_messaggio];
            } else if (tipo == 2) {
                messaggioLog = errors_log[indice_messaggio];
            }
            proxy.log(messaggioLog, tipo);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
