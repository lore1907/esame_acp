package hello;

import java.util.concurrent.TimeUnit;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class HelloClient {
    
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloClient(Channel channel) {

        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        System.out.println("Will try to greet " + name + "...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;

        try{
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failes: {0}" + e.getStatus());
            return;
        }

        System.out.println("Greeting: " + response.getMessage());
    }



    public static void main(String[] args) throws Exception {
        String user = "world";
        String target = "localhost:" + args[0];

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        try{
            HelloClient client = new HelloClient(channel);
            client.greet(user);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

    }
}
