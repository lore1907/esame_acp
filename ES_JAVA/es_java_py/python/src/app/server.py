from concurrent import futures
from multiprocess import Queue
import grpc
from generated import magazzino_pb2
from generated import magazzino_pb2_grpc

import logging

logging.basicConfig(filename="magazzino.log", level=logging.DEBUG, format="[SERVER] %(asctime)s - %(levelname)s - %(message)s")

class Magazzino(magazzino_pb2_grpc.MagazzinoServicer):

    def __init__(self, queue): 
        self.queue = queue

    def preleva(self, request, context): 
        result = self.queue.get()
        logging.debug("Valore prelevato: %s", result)

        return magazzino_pb2.Articolo(valore=result)

    def deposita(self, request, context):

        self.queue.put(request.valore)
        logging.debug("Valore depositato: %s", request.valore)

        return magazzino_pb2.Empty()



def serve(): 
    
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    queue = Queue(5)

    magazzino_pb2_grpc.add_MagazzinoServicer_to_server(Magazzino(queue), server)

    port = server.add_insecure_port("[::]:0")

    server.start()

    logging.info("Server starded, listening on " + str(port))

    server.wait_for_termination()


if __name__ == "__main__": 
    serve()

        