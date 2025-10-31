from services import Services
from implementations import Implementations
import socket, multiprocess


def run_fun(c, skel): 

    data = c.recv(1024)
    message = data.decode("utf-8")
    print(f"Richiesta ricevuta:  {message}")

    tipo = message.split("-")[0]

    if tipo == "deposita": 
        id_articolo = message.split("-")[1]
        skel.deposita(id_articolo)
        result = "deposited"
        
    elif tipo == "preleva": 
        result = str(skel.preleva())
    else: 
        print("Richiesta non eseguibile...")
        result = "error:bad request"

    message_to_send = result.encode("utf-8")
    c.send(message_to_send)

    c.close()
    

class Skeleton(Services): 

    def __init__(self, ip, port, delegate): 
        self.ip = ip
        self.port = port
        self.delegate = delegate

    def preleva(self): 
        return self.delegate.preleva()

    def deposita(self, id_articolo): 
        self.delegate.deposita(id_articolo)

    def run_skel(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.ip, self.port))
        print(f"[SKELETON] binded to {self.ip}:{str(self.port)}")

        s.listen(30)

        while True: 
            c, addr = s.accept()
            print("Connected to ", addr[0], ":", addr[1])

            t = multiprocess.Process(target=run_fun, args=(c, self))
            t.start()

        s.close()