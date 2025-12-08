package bookingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import services.IBooking;

public class Skeleton implements IBooking{

    private IBooking delegate;
    private int port;

    protected Skeleton (IBooking delegate, int port){
        this.delegate = delegate;
        this.port = port;
    }
    
    public void run_skel(){

        ServerSocket server;
        Socket client = null; 
        try {
            server = new ServerSocket(port);
            try{
            
                while(true){
                    client = server.accept(); 
                    BookingThread thread = new BookingThread(client, delegate);
                    thread.start();
                }
    
            } catch (Exception e)  {
                System.out.println("Richiesta chiusura....");
                server.close();
                if (client != null){
                    client.close();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    @Override
    public void book(String user, int people, String timeSlot) {
        // TODO Auto-generated method stub
        delegate.book(user, people, timeSlot);
    }

    
    
}
