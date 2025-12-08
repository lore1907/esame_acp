package services;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.jms.*;
public class BookingImpl implements IBooking{

    private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
    

    public BookingImpl(QueueConnection qconn, Queue booking){

        Consumatore cons = new Consumatore(qconn, booking);
        cons.start();

    }


    @Override
    public void book(String user, int people, String timeSlot) {
        
        String message = user + "-" + people + "-" + timeSlot;
        
        try{
            Thread.sleep(1000);
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private class Consumatore extends Thread{

        private QueueConnection qconn;
        private Queue booking;

        private Consumatore(QueueConnection qconn, Queue booking){
            this.qconn = qconn;
            this.booking = booking;
        }

        public void run(){
            
            String message;

            QueueSession qsession = null; 
            QueueSender qsender = null;
            TextMessage msg;

            try{
                qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                qsender = qsession.createSender(booking);

                while(true){
                    message = queue.take();

                    msg = qsession.createTextMessage(message); 
                    qsender.send(msg);
                }


            } catch (InterruptedException e){
                e.printStackTrace();
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {

                try{
                    msg = qsession.createTextMessage("STOP");
                    qsender.send(msg);

                    if (qsender != null) {
                        qsender.close();
                    }
                    if (qsession != null) {
                        qsession.close();
                    }
                }catch(JMSException e){
                    e.printStackTrace();
                }
            }


    }
}

}
