import grpc, sys, random
import manager_pb2, manager_pb2_grpc
import threading
tipi_richiesta = ["buy", "sell"]
NUM_THREADS = 10

def thread_fun(port, req): 

    channel = grpc.insecure_channel('localhost:' + str(port))
    stub = manager_pb2_grpc.ManagerStub(channel)
    print(f"Ricevuta richiesta {req}")

    if req == "buy": 
        response = stub.buy(manager_pb2.Empty())
    else: 
        print(f"Ricevuta richiesta di buy, genero serial_number...")
        serial_number = random.randint(1,100)
        response = stub.sell(manager_pb2.Product(id=serial_number))

    print(f"Risultato per richiesta {req}. Risultato = [{response}]")



if __name__ == "__main__":

    try:
        PORT = sys.argv[1]
    except IndexError: 
        print("Inserire numero di porta...")
        sys.exit(1)

    threads = []

    for i in range(NUM_THREADS): 
        req = random.choice(tipi_richiesta)
        t = threading.Thread(target=thread_fun, args=(PORT, req))
        t.start()
        threads.append(t)

    for thread in threads: 
        thread.join()


