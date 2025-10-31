package manager;

import javax.jms.*;

import java.util.logging.*;

public class ManagerListener implements MessageListener{

    
    private static final Logger logger = Logger.getLogger(ManagerListener.class.getName());
    private TopicConnection tconn;
    private Topic topic_stats; 
    private Topic topic_tickets; 

    public ManagerListener (TopicConnection tconn, Topic t1, Topic t2){
        this.tconn = tconn;
        this.topic_tickets = t1;
        this.topic_stats = t2;
    }


    @Override
    public void onMessage(Message m){
        
        if (m instanceof MapMessage) {

            MapMessage mMess = (MapMessage)m; 

            logger.log(Level.INFO, "---------- LISTENER IN ASCOLT0 ----------");

            ManagerThread mt = new ManagerThread(tconn, topic_stats, topic_tickets, mMess); 
            mt.start();
        }  

    }


}
