package server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.jms.*;


public class ServiceImpl extends Skeleton{

    private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
    private final ManagerImpl manager; 
    private int totalReservations = 0;


    protected ServiceImpl(int p, QueueConnection qconn, Queue booking, ManagerImpl manager) {
        super(p);
        this.manager = manager;
        //TODO Auto-generated constructor stub
        Consumatore consumatore = new Consumatore(queue, qconn, booking);
        consumatore.start();
    }

    @Override
    public void book(String user, int people, String timeSlot) {
        
        String message = user + "-" + people + '-' + timeSlot;
        try {
            queue.put(message);
            totalReservations++;
            manager.setTotalReservations(totalReservations);
            manager.setLastReservations(user, people, timeSlot);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    private class Consumatore extends Thread{

        private QueueConnection qconn;
        private Queue b_queue;
        private Consumatore(BlockingQueue<String> q, QueueConnection qconn, Queue b_queue){
            queue = q;
            this.qconn = qconn;
            this.b_queue = b_queue;
        }

        public void run(){
            QueueSession qsession = null;
            QueueSender qsender = null;

            try{
                String message = queue.take();

                qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                qsender = qsession.createSender(b_queue); 
                TextMessage msg = qsession.createTextMessage(message);

                qsender.send(msg);
                
            } catch (JMSException e){
                System.out.println("Riscontrato errore JMS, Chiudo le risorse...."); 
                
                try{
                    if(qsender !=null) qsender.close();
                    if(qconn !=null) qconn.close();
                }catch(JMSException ex){
                    ex.printStackTrace();
                }

                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
