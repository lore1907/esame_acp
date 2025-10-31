from error_service import ErrorService
import socket 
import multiprocess as mp
from multiprocess import Value

def run_fun(c, skel):

    data = c.recv(1024).decode("utf-8")

    header = data.split("-")[0]


    if header.upper() == "ERROR":
        skel.writeError(data)
        with skel.count.get_lock():
            skel.count.value += 1
            print(f"[ERROR SERVER] ricevuti {skel.count.value} messaggi..")
        result = "ACK"
    else : 
        print(f"[Log Manager] sended wrong message: {data}")
        result = "NACK"

    c.send(result.encode("utf-8"))
    
    c.close()


class Skeleton(ErrorService): 

    def __init__(self, host, port, service):
        
        self.host = host 
        self.port = port
        self.count = Value("i", 0)
        self.service = service

    def writeError(self, message): 
        self.service.writeError(message)

    
    def run_skel(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        #host = "localhost"

        s.bind((self.host, self.port))
        self.port = s.getsockname()[1]
        print(f"[SKELETON] Listening on port {self.port}")

        s.listen(20)

        while True: 

            c, addr = s.accept()
            print(f"[SKELETON] Totale messagi ricevuti finora: {self.count.value}")
            t = mp.Process(target=run_fun, args=(c, self), daemon = True)
            t.start()

        s.close()