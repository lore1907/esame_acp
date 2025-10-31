import grpc, manager_pb2, manager_pb2_grpc
import multiprocess as mp 
from threading import Lock
from concurrent import futures 

class Server(manager_pb2_grpc.ManagerServicer): 

    def __init__(self, queue, lock_p, lock_d):

        self.queue = queue
        self.lock_p = lock_p
        self.lock_d = lock_d
        
    def preleva(self, request, context): 

        with self.lock_p: 
            result = self.queue.get()
        
        print( f"[SERVER] Prelevato [{str(result[0])}, {result[1]}]" )

        return manager_pb2.Product(id=result[0], product=result[1])

    def deposita(self, request, context): 

        with self.lock_d: 
            self.queue.put([request.id, request.product])

        print( f"[SERVER] Depositato [{str(request.id)}, {request.product}]" )

        return manager_pb2.StringMessage(value="deposited")


    def svuota(self, request, context): 

        self.lock_d.acquire()
        self.lock_p.acquire()

        while  not self.queue.empty(): 
            result = self.queue.get()
            print(f"[SERVER] Prelevato [{str(result[0])}, {result[1]}]")

            yield manager_pb2.Product(id=result[0], product=result[1])

        self.lock_d.release()
        self.lock_p.release()


if __name__ == "__main__": 

    q = mp.Queue(5)
    lock_d = Lock()
    lock_p = Lock()

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    manager_pb2_grpc.add_ManagerServicer_to_server(Server(q, lock_p, lock_d), server)

    port = 0 

    port = server.add_insecure_port('[::]:' + str(port))
    print("Starting server. Listening on port " + str(port))

    server.start()

    print("Server running...")

    server.wait_for_termination()

