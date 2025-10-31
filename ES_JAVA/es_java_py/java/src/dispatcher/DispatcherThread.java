package dispatcher;

import java.util.logging.*;

import javax.jms.*;

import io.grpc.*;


public class DispatcherThread extends Thread{
    
    private MagazzinoGrpc.MagazzinoBlockingStub blockingStub;
    private QueueConnection qconn;
    private TextMessage msg;
    private static final Logger logger = Logger.getLogger(DispatcherThread.class.getName());

    public DispatcherThread (MagazzinoGrpc.MagazzinoBlockingStub bs, QueueConnection qc, TextMessage m) {

        blockingStub = bs;
        qconn = qc;
        msg = m;
    }

    public void run() {
        logger.log(Level.INFO, "Thread avviato...");

        try {

            String message = msg.getText();

            logger.info("Messaggio ricevuto: " + message);
            logger.info("JMSReplyTo: " + msg.getJMSReplyTo());

            Queue qresponse = (Queue)msg.getJMSReplyTo();

            String result = null;

            if (message.equalsIgnoreCase("preleva")){
                
                logger.info("Ricevuta richiesta prelievo");
                Empty empty = Empty.newBuilder().build();
                Articolo articolo = null;

                try {
                    articolo = blockingStub.preleva(empty);
                    result = new String(Long.toString(articolo.getValore()));
                } catch (StatusRuntimeException e) {
                    logger.log(Level.SEVERE, "RPC failed: {0} " , e.getStatus());
                    result = new String("-1");
                }
            }

            else if (message.toLowerCase().contains("deposita")) {
                
                String[] splitted = message.split("-");
                if(splitted.length != 2){
                    logger.log(Level.WARNING, "Bad Format: " + message);
                    return;
                }
                Integer valoreDaDepositare = Integer.valueOf(splitted[1]);

                logger.info("Ricevuta richiesta di deposito. Valore da depositare: " + valoreDaDepositare);

                Articolo articolo = Articolo.newBuilder().setValore(valoreDaDepositare).build();

                try {
                    blockingStub.deposita(articolo);
                    result = new String("deposited");
                } catch (StatusRuntimeException e) {
                    logger.log(Level.SEVERE, "RPC failed: {0} " , e.getStatus());
                    result = new String("not deposited");
                }

            }
            
            QueueSession qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            TextMessage response_message = qsession.createTextMessage(result);
            QueueSender sender = qsession.createSender(qresponse);

            sender.send(response_message);
            sender.close();

            qsession.close();
         

        } catch (JMSException e) {
            logger.log(Level.SEVERE, "GetText fallita, JMSException:  ", e);
        }
    }
}
