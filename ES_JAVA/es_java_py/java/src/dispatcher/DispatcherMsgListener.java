package dispatcher;

import java.util.logging.*;

import javax.jms.*;


public class DispatcherMsgListener implements MessageListener{
    
    private QueueConnection qconn;
    private MagazzinoGrpc.MagazzinoBlockingStub blockingStub;
    private static final Logger logger = Logger.getLogger(DispatcherMsgListener.class.getName());

    public DispatcherMsgListener (QueueConnection qc, MagazzinoGrpc.MagazzinoBlockingStub bs){
        qconn = qc;
        blockingStub = bs;
    }
   

    @Override
    public void onMessage(Message m) {

        TextMessage msg = (TextMessage)m;
        logger.log(Level.INFO, "Listener in ascolto...");
        DispatcherThread dt = new DispatcherThread(blockingStub, qconn, msg);
        dt.start();
    }
}
