package dispatcher;


import javax.jms.*;
import javax.naming.*;


import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import logging.LoggerUtil;

public class Dispatcher {
    
    private static final Logger logger = Logger.getLogger(Dispatcher.class.getName());
    public static void main(String[] args) throws Exception {
        
       LoggerUtil.setup();
    
        if (args.length != 1) {
            logger.log(Level.WARNING, "Specificare il numero di porto");
            System.exit(-1);
        }

        Hashtable<String, String> prop = new Hashtable<String, String>();
        
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        
        prop.put("queue.request", "request");
        prop.put("queue.response", "response");

        ManagedChannel channel = null;
         
        try {

            Context jndiContext = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");

            Queue qrequest = (Queue) jndiContext.lookup("request");
            QueueConnection qc = qcf.createQueueConnection();
            qc.start();

            QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver receiver = qs.createReceiver(qrequest);

            int port = Integer.valueOf(args[0]);
            String target = "localhost:" + port;
            
            channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();

            MagazzinoGrpc.MagazzinoBlockingStub blockingStub = MagazzinoGrpc.newBlockingStub(channel);
            DispatcherMsgListener listener = new DispatcherMsgListener(qc, blockingStub);

            receiver.setMessageListener(listener);

            logger.info("Dispatcher avviato - comunicazione lato server su porto: " + port);
            logger.info("In atteesa di messagi. Premere Ctrl + C per terminare");
            
            ManagedChannel chFinal = channel; 
            Connection qConnFinal = qc;

            CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread( () -> {
                try {
                    logger.info("Shutdown in corso...");

                    //Chiude anche sessione e receiver
                    if (qConnFinal != null) qConnFinal.close();
                    if (chFinal != null) chFinal.shutdownNow().awaitTermination(20, TimeUnit.SECONDS);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Errore in chiusura delle risorse ", e);
                } finally {
                    latch.countDown();
                }
            }));
            latch.await();
            //Blocco per tenere l' applicazione aperta finche non viene terminata
            //Thread.currentThread().join();


        } catch (NamingException e) { 
            logger.log(Level.SEVERE, "Lookup fallito, Naming exception generata: ", e);

        } catch (JMSException e) {    
            logger.log(Level.SEVERE, "Creazione connessione fallita, JMSexception generata: ", e);

        } 

    }

}



