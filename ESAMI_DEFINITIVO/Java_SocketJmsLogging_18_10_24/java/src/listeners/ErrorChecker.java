package listeners;
import java.io.FileWriter;
import java.io.IOException;

import javax.jms.*;

public class ErrorChecker implements MessageListener{
    
    private String check;

    public ErrorChecker(String check){
        this.check = check;
    }

    @Override
    public void onMessage(Message message) {
        try{
        
            MapMessage msg = (MapMessage) message;
            String messaggioLog = msg.getString("messaggioLog");
            System.out.println("Ricevuto messaggio " + messaggioLog);
            if (messaggioLog.contains(check)){ 
                
                String line = messaggioLog + "\n";
                try {
                    FileWriter file = new FileWriter("error.txt", true);
                    file.write(line);
                    file.flush();
                    file.close();
                } catch (IOException e){ 
                    System.out.println("[{errors.txt}] Errore nella chiusura del file");
                }
                
            } else {
                System.out.println("[ERROR CHECKER] Il messaggio non rispetta la severita richiesta.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
