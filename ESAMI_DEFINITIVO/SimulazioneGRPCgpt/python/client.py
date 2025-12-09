import grpc
import bookingService_pb2, bookingService_pb2_grpc


def run(): 


    with grpc.insecure_channel("localhost:50051") as channel: 

        stub = bookingService_pb2_grpc.BookingManagerStub(channel=channel)

        try:
            while(True): 
                last_reservation = stub.GetLastReservation(bookingService_pb2.Empty())
                total_reservations = stub.GetTotalReservations(bookingService_pb2.Empty())
                
                print(f"NUMERO PRENOTAZIONI: {total_reservations}")
                print(f"Ultima prenotazione: {last_reservation}")

        except (KeyboardInterrupt):
            return



if __name__ == "__main__":

    run()