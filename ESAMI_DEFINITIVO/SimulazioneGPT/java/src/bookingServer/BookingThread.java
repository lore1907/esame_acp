package bookingServer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import services.IBooking;

public class BookingThread extends Thread{
    
   
    private Socket socket; 
    private IBooking delegate;

    protected BookingThread(Socket s, IBooking delegate){
        socket = s;
        this.delegate = delegate;
    }

    public void run(){
        
        try{
            DataInputStream dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String user = dataIn.readUTF();
            int people = dataIn.readInt();
            String orario = dataIn.readUTF();

            System.out.println("Ricevuta prenotazione: user: " + user + ", num_people: " + people + ", orario: " + orario);
            delegate.book(user, people, orario);

        } catch (IOException e) {
            e.printStackTrace();
            try{
                socket.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

    }
}
