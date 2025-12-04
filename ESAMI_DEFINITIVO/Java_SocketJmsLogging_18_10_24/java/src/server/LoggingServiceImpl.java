package server;
import javax.jms.*;


public class LoggingServiceImpl extends ServerSkeleton{
    
    private QueueConnection connection;
    private Queue queue_error; 
    private Queue queue_info;

    public LoggingServiceImpl(int p, QueueConnection conn, Queue queue_error, Queue queue_info{
        super(p);
        connection = conn;
        this.queue_error = queue_error;
        this.queue_info = queue_info;
    }

    @Override
    public synchronized void log(String messaggioLog, int tipo){
        System.out.println("");
        LogThread produttore = new LogThread(messaggioLog, tipo);
        produttore.start();
    }

    private class LogThread extends Thread{

        private String messaggioLog; 
        private int tipo; 
        

        private LogThread(String mLog, int t){
            messaggioLog = mLog;
            tipo = t;
        }

        @Override
        public void run(){
            try{ 
                QueueSession qs = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                MapMessage msg = qs.createMapMessage(); 
                msg.setString("messaggioLog", messaggioLog);
                msg.setInt("tipo", tipo);

                if (tipo == 2){
                    QueueSender error_sender = qs.createSender(queue_error);
                    error_sender.send(msg);
                } else {
                    QueueSender info_sender = qs.createSender(queue_info);
                    info_sender.send(msg);
                }

            }catch (JMSException e){
                e.printStackTrace();
            }
        }
    }
}
