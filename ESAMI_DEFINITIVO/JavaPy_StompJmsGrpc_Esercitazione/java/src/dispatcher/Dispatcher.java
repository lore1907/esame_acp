package dispatcher;
import io.grpc.*;
import javax.jms.*;
import javax.naming.*;
import javax.naming.Context;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import generated.ServiceGrpc;
import generated.ServiceGrpc.ServiceBlockingStub;
import generated.Empty;
import generated.Product;

public class Dispatcher {
    
    private ServiceBlockingStub blockingStub;
    
    public Dispatcher(Channel ch){
        blockingStub = ServiceGrpc.newBlockingStub(ch);
    }

    public void deposita(long id_articolo){
        System.out.println("Deposito articolo: " + id_articolo);
        Product articolo = Product.newBuilder().setIdArticolo(id_articolo).build();
        Empty response;

        try{
            response = blockingStub.deposita(articolo);
        } catch (StatusRuntimeException e){
            System.out.println("RPC failed: {0}" + e.getStatus());
            return;
        }

    }

    public long preleva(){
        System.out.println("Richiesta di prelievo invocata."); 
        Empty request = Empty.newBuilder().build();
        Product response;
        long id_articolo;

        try{
            response = blockingStub.preleva(request);
            id_articolo = response.getIdArticolo();
        } catch (StatusRuntimeException e){
            System.out.println("RPC failed: {0}" + e.getStatus());
            return -1;
        }
        return id_articolo;
    }

   

    public static void main(String[] args) throws InterruptedException{
        
        if (args.length != 1){
            System.out.println("Si prega di inserire numero di porta..");
            System.exit(1);
        }

        String target = "localhost:" + args[0];
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();


        Hashtable<String, String> prop = new Hashtable<String,String>(); 
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        
        prop.put("queue.response", "response");
        prop.put("queue.request", "request");

        QueueConnection qconn = null;
        QueueSession qsess = null;
        QueueReceiver qreceiver = null;

        try{
            Context ctx = new InitialContext(prop);
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue request = (Queue) ctx.lookup("request");
            qconn = qcf.createQueueConnection(); 
            qconn.start();
            qsess = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            qreceiver = qsess.createReceiver(request);
           

            Dispatcher dispatcher = new Dispatcher(channel);
            qreceiver.setMessageListener(new DispatcherListener(qconn, dispatcher));


            try {
                while(true){
                    Thread.sleep(1000);
                } 
            } catch (InterruptedException e) {
                System.out.println("Richiesta chiusura....");
            }
            
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            
            try{
                if(qsess!=null)
                    qsess.close();
                if(qconn!= null)
                    qconn.close();
            }catch (JMSException e) {
                e.printStackTrace();
            }

            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
