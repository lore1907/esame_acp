package listeners;
import java.io.FileWriter;
import java.io.IOException;

import javax.jms.*;

public class InfoFilter implements MessageListener{

    @Override
    public void onMessage(Message message) {
        
        if (message instanceof MapMessage) {
            MapMessage msg = (MapMessage)message;
       

            try{
                String messaggioLog = msg.getString("messaggioLog"); 
                int tipo = msg.getInt("tipo"); 
            
                if (tipo == 1){
                    System.out.println("Ricevuto messaggio con tipo richiesto, :" + messaggioLog);
                    String line = messaggioLog + "\n";
                    
                    try {
                        FileWriter fw = new FileWriter("info.txt", true);
                        fw.write(line);
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        // TODO: handle exception
                    }

                } else {
                    System.out.println("Messaggio di DEBUG ricevuto.");
                    return;
                }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
