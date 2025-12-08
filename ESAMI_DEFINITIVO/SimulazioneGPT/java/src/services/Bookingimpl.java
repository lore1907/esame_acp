package services;
import javax.jms.*;
import javax.naming.*;

import java.util.Dictionary;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Bookingimpl implements IBooking{

    private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);

    private final QueueConnection qconn; 
    private final Queue booking_queue;
    
    public Bookingimpl(QueueConnection qconn, Queue booking_queue){
        this.qconn = qconn;
        this.booking_queue = booking_queue;

        Consumatore consumatore = new Consumatore(queue, qconn, booking_queue);
        consumatore.start();
    }
    
    @Override
    public void book(String user, int people, String timeSlot) {
        
        String message = user + "-" + people + "-" + timeSlot;
        try{
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    private class Consumatore extends Thread{
        
        private BlockingQueue<String> queue;
        private QueueConnection qconn;
        private Queue booking_queue;
        
        private Consumatore(BlockingQueue<String> queue, QueueConnection qconn, Queue booking_queue){
            this.queue = queue;
            this.qconn = qconn;
            this.booking_queue = booking_queue;
        }

        public void run(){
            
            QueueSession qsession = null;
            QueueSender sender = null;
            boolean condition = true;
            TextMessage msg;

            try{ 
                qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                sender = qsession.createSender(booking_queue); 

                while (condition) {
                    String message = queue.take();
                    
                    msg = qsession.createTextMessage(message);
                    sender.send(msg);
                    if(message.equals("STOP")){
                        condition = false;
                    }
                }

            } catch (JMSException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
            } finally {

                try{
                    if(sender!=null){
                        sender.close();
                    }
                    if(qsession!=null){
                        qsession.close();
                    }

                } catch (JMSException e){
                    e.printStackTrace();
                }
                
            }
        }
    }
}
