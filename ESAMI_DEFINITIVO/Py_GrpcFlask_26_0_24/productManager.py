import manager_pb2, manager_pb2_grpc
import grpc, time
from threading import Lock, Condition
from concurrent import futures
import requests


DIM = 5


def an_item_is_available(queue): 
    return not (len(queue) == 0)

def a_space_is_available(queue):
    return not (len(queue) == DIM)

    
class ProductManager(manager_pb2_grpc.ManagerServicer): 

    def __init__(self, coda): 
        self.laptop_queue = coda 
        self.lock = Lock()
        self.sell_cv = Condition(lock=self.lock)
        self.buy_cv = Condition(lock=self.lock)

    def sell(self, request, context): 
        id_articolo = request.id 

        with self.sell_cv: 

            while not a_space_is_available(self.laptop_queue): 
                self.sell_cv.wait()
            
            time.sleep(1.0)
            self.laptop_queue.append(id_articolo)
            print(f"Inserito item in coda con id: {id_articolo}")

            self.buy_cv.notify()

        log_to_history("sell", id_articolo)
        
        return manager_pb2.StringMessage(value="ack") 


    def buy(self, request, context): 
        
        with self.buy_cv: 

            while not an_item_is_available(self.laptop_queue): 
                self.buy_cv.wait()

            time.sleep(1.0)
            item = self.laptop_queue.pop(0)
            print(f"Prelevato item dalla coda con id: {item}")

            self.sell_cv.notify()

        log_to_history("buy", item)

        return manager_pb2.Product(id=item)
        
    def log_to_history(self, operation, serial_number): 
        message = {"operation": operation, "serial_number": serial_number}
        resourse_location =  "http://127.0.0.1:5001" + "/update_history"
        response = requests.post(resourse_location, json = message)

        try: 
            response.raise_for_status()

        except requests.exceptions.HTTPError:
            print(f"[{operation} - {item}] Errore ricevuto {response.status_code} - {response.text}")
        else: 
            print(f"[{operation} - {item}] Avvenuto con successo")

def serve(): 

    coda = [] 

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10), options=(('grpc.so_reuseport', 0),))

    manager_pb2_grpc.add_ManagerServicer_to_server(ProductManager(coda), server)
    port = server.add_insecure_port("[::]:0")

    server.start()
    
    print("Server started, listening on " + str(port))

    server.wait_for_termination()



if __name__ == "__main__": 
    serve()