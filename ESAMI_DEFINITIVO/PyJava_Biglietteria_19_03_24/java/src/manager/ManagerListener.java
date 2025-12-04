package manager;
import javax.jms.*;
import javax.naming.*;
import java.io.*;

public class ManagerListener implements MessageListener{

    private TopicSession session;
    private TopicPublisher publisher_stats;
    private TopicPublisher publisher_tickets;

    public ManagerListener(TopicSession session, TopicPublisher publisher_stats, TopicPublisher publisher_tickets){
        this.session = session;
        this.publisher_stats = publisher_stats;
        this.publisher_tickets = publisher_tickets;
    }

    @Override
    public void onMessage(Message m) {
        
        try{

            MapMessage msg = (MapMessage)m;
            System.out.println("Messaggio ricevuto: " +  msg.getString("type") + msg.getString("value"));
            String type = msg.getString("type");
            if (type.toUpperCase().equals("STATS")){
                String value = msg.getString("value");
                TextMessage msg_to_send = session.createTextMessage(value);
                publisher_stats.publish(msg_to_send);
            } else if (type.toUpperCase().equals("BUY")){
                String value = msg.getString("value");

                try{
                    BufferedWriter writer = new BufferedWriter(new FileWriter("tickets.txt", true));
                    writer.newLine();
                    writer.write("Artista: " + value);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TextMessage msg_to_send = session.createTextMessage(value);
                publisher_tickets.publish(msg_to_send);
            } else {
                System.out.println("Errore, tipo non riconosciuto");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } 
    }
    

}
