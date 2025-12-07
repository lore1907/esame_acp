package dispatcher;
import javax.jms.*;



public class DispatcherListener implements MessageListener{

    private Dispatcher dispatcher;
    private QueueConnection connection;

    public DispatcherListener(QueueConnection qconn, Dispatcher dispatcher){
        this.connection = qconn;
        this.dispatcher = dispatcher;
    }

    @Override
    public void onMessage(Message arg0) {
        TextMessage msg = (TextMessage)arg0;
        try{
            
            System.out.println("Ricevuta richiesta: " + msg.getText());
            System.out.println("Rispondo sulla coda " + msg.getJMSReplyTo() + "...");

            DispatcherThread t = new DispatcherThread(connection, dispatcher, msg);
            t.start();
        } catch (JMSException e){
            e.printStackTrace();
        }
    }
    
}
