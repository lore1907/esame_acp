package clusterManager;


import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import generated.*;

import javax.naming.Context;



import javax.jms.*;
import javax.naming.*;

import io.grpc.stub.StreamObserver;
import logger.LoggerUtil;

public class ClusterManager extends ManagerGrpc.ManagerImplBase{

    public static final Logger logger = Logger.getLogger(ClusterManager.class.getName());
    public final  TopicSession ts;
    public final TopicPublisher pubGpu;
    public final TopicPublisher pubRt;      

    public ClusterManager(TopicSession ts, TopicPublisher pubGpu, TopicPublisher pubRt ){

        LoggerUtil.setup();
        this.ts = ts; 
        this.pubGpu = pubGpu;
        this.pubRt = pubRt;
    }

    @Override
    public void deploy(Task task, StreamObserver<Empty> responObserver){
        logger.log(Level.INFO, "Richiesta ricevuta per il task: " + task.getName());

    }


    public static void main(String[] args) {
        
        Hashtable<String, String > prop = new Hashtable<String, String>();

        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.gpu", "gpu");
        prop.put("topic.rt", "rt");
        
        try{
            Context jndiContext = new InitialContext(prop);

            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            Topic rl_topic = (Topic) jndiContext.lookup("rt");
            Topic gpu_topic = (Topic) jndiContext.lookup("gpu");

            TopicConnection tc = tcf.createTopicConnection();
            TopicSession ts = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            TopicPublisher pub_rt = ts.createPublisher(rl_topic);
            TopicPublisher pub_gpu = ts.createPublisher(gpu_topic);

            TextMessage text = ts.createTextMessage();


        } catch (JMSException|NamingException e) {
            e.printStackTrace();
        }
        
    }
}
