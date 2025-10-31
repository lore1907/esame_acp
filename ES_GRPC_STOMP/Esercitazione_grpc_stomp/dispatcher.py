import time, sys 
import grpc, stomp
import manager_pb2, manager_pb2_grpc

from multiprocess import Process 

def proc_fun(port, mess): 

    request = mess.split('-')[0]

    conn = stomp.Connection([('127.0.0.1', 61613)])

    conn.connect(wait=True)

    channel = grpc.insecure_channel('localhost:'+str(port), options=(('grpc.so_reuseport', 1),))

    stub = manager_pb2_grpc.ManagerStub(channel)

    if request == "deposita": 
        id_p = mess.split('-')[1]
        prodotto = mess.split('-')[2]
        result = stub.deposita(manager_pb2.Product(id=int(id_p), product=prodotto))
        print("[DISPATCHER] Response:", result)
        conn.send("/queue/response", result.value)

    elif request == "preleva": 
        prodotto = stub.preleva(manager_pb2.Empty())
        print("[DISPATCHER] Response:", str(prodotto))
        conn.send("/queue/response", str(prodotto.id) + "-" + prodotto.product)

    elif request == "svuota": 
        for result in stub.svuota(manager_pb2.Empty()): 
            print('[DISPATCHER] Response:', str(result))
            conn.send("/queue/response", str(result.id) + "-" + result.product)


class MyListener(stomp.ConnectionListener): 

    def __init__(self, port): 
        self.port = port

    def on_message(self, frame): 
        print('Request recieved: "%s"' %frame.body)

        p = Process(target=proc_fun, args=(self.port, frame.body))
        p.start()

if __name__  == "__main__": 

    try: 
        PORT = sys.argv[1]
    except IndexError: 
        print("Please, specify arg port")

    conn = stomp.Connection([('127.0.0.1', 61613)])

    conn.set_listener("", MyListener(PORT))

    conn.connect(wait=True)
    conn.subscribe(destination="/queue/request", id=1, acl="auto")
    
    print("[DISPATCHER] Waiting for requests ...")
    
    while True: 
        time.sleep(60)