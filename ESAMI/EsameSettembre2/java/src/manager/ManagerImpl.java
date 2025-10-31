package manager;
import generated.*;
import io.grpc.*;
import java.util.logging.*;
import io.grpc.stub.StreamObserver;
import generated.ManagerGrpc;

public class ManagerImpl extends ManagerGrpc.ManagerImplBase {

    private static final Logger logger = Logger.getLogger(ManagerImpl.class.getName());

    @Override 
    public void deploy(DeployRequest req, StreamObserver<Empty> responseObserver){
        logger.log(Level.INFO, "Ricevuta richiesta DEPLOY per task: ID=" + req.getId() + ", Tipo=" +req.getTaskType() );
        Thread t = new ClusterManagerThread();
        t.start();

        Empty empty = Empty.newBuilder().build();
        responseObserver.onNext(empty);
        responseObserver.onCompleted();
    }

    @Override 
    public void stopAll(StopRequest req, StreamObserver<Empty> responseObserver){

    }
}
