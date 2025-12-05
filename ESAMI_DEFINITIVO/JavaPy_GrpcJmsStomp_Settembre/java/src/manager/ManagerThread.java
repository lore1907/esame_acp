package manager;
import io.grpc.*;
import generated.*;
import javax.jms.*;
import javax.naming.*;


public class ManagerThread extends Thread{
    
    private DeployRequest dRequest; 
    private StopRequest sRequest;
    private String message;
    private QueueConnection qconn;
    private Queue gpu_queue;
    private Queue rt_queue;
    

    public ManagerThread(QueueConnection qc, Queue gpu, Queue rt, DeployRequest dreq, String msg){
        qconn = qc;
        gpu_queue = gpu;
        rt_queue = rt;
        dRequest = dreq;
        sRequest = null;
        message = msg;
    }

    public ManagerThread(QueueConnection qc, Queue gpu, Queue rt, StopRequest sreq){
        qconn = qc;
        gpu_queue = gpu;
        rt_queue = rt;
        sRequest = sreq; 
        dRequest = null;
    }


    @Override
        public void run(){ 
        
        try{
            QueueSession qs = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); 
            QueueSender queue_sender;
            TextMessage msg_to_send; 

            if(dRequest != null){
                String tipo = dRequest.getTipo();
                msg_to_send = qs.createTextMessage(message);

                if (tipo.equalsIgnoreCase("gpu-bound")){
                    queue_sender = qs.createSender(gpu_queue);

                } else if (tipo.equalsIgnoreCase("real-time")){
                    queue_sender = qs.createSender(rt_queue);
                } else {
                    System.out.println("Ricevuto messaggio con tipo errato. Tipo ricevuto: " + tipo);
                    return;
                }

                queue_sender.send(msg_to_send);
            
            } else if (sRequest != null) {
                String tipo = sRequest.getTipo();
                msg_to_send = qs.createTextMessage("stopAll"); 

                if (tipo.equalsIgnoreCase("gpu-bound")){
                    queue_sender = qs.createSender(gpu_queue);

                } else if (tipo.equalsIgnoreCase("real-time")){
                    queue_sender = qs.createSender(rt_queue);
                } else {
                    System.out.println("Ricevuto messaggio con tipo errato. Tipo ricevuto: " + tipo);
                    return;
                }

                queue_sender.send(msg_to_send);
                
            } 

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
