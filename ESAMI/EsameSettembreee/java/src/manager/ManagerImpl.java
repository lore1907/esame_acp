package manager;
import generated.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

public class ManagerImpl extends ManagerGrpc.ManagerImplBase{
    

    @Override
    public void deploy(DeployReq req, StreamObserver<Empty> responseObserver){
        System.out.println("[MANAGER] Invoked Deploy...");
        
        Thread t = new ManagerThread(req);
        t.start();

        Empty reply = Empty.newBuilder().build(); 
        responseObserver.onNext(reply);
        responseObserver.onCompleted(); 
    }


    @Override   
    public void stopAll(StopReq req, StreamObserver<Empty> responseObserver){
        System.out.println("[MANAGER] Invoked StopAll...");
        
        Thread t = new ManagerThread(req);
        t.start();

        Empty reply = Empty.newBuilder().build(); 
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
