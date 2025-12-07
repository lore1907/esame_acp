import grpc 
import service_pb2 as pb2 
import service_pb2_grpc as pb2_grpc 
import time
import multiprocess as mp 

from concurrent import futures


class Server(pb2_grpc.ServiceServicer): 

    def __init__(self, queue):
        self.queue = queue

    def Preleva(self, request, context):
        print(f"Ricevuta richiesta di prelievo articolo. Prelevo...")
        
        try:
            articolo = self.queue.get(block=True, timeout=5)
            print(f"Articolo {articolo} prelevato correttamente.")
        except self.queue.Empty:
            print("Nessun articolo disponibile per il prelievo")
            return pb2.Product(id_articolo=-1)

        return pb2.Product(id_articolo=articolo) 
        

    def Deposita(self, request, context):
        print(f"Ricevuta richiesta di deposito articolo con id: {request.id_articolo}")
        articolo = request.id_articolo
        self.queue.put(articolo)

        return pb2.Empty()



def serve():

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10), options=(('grpc.so_reuseport',0),))
    queue = mp.Queue(5)
    port = 0 
    pb2_grpc.add_ServiceServicer_to_server(Server(queue), server)
    cur_port = server.add_insecure_port("[::]:" + str(port))

    server.start()
    print(f"Server started, listening on port: " + str(cur_port))
    print(f"---> Start Dispatcher Java with: Java Dispatcher {cur_port}")

    server.wait_for_termination()



if __name__ == "__main__": 

   serve()