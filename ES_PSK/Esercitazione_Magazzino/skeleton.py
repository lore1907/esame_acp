from abc import ABC, abstractmethod
from i_magazzino import IMagazzino
import socket, threading


def thd_func(c, skel): 

    data = c.recv(1024)
    message = data.decode("utf-8")

    service = message.split('-')[0]
    articolo = message.split('-')[1]

    result = None

    if service == "preleva": 
        result = skel.preleva(articolo)
    elif service == "deposita": 
        id = message.split('-')[2]
        result = skel.deposita(articolo, id)
    else: 
        print("Service non riconosciuto")


    response = str(result)
    c.send(response.encode("utf-8"))

    c.close()

class Skeleton(IMagazzino, ABC): 

    def __init__(self, ip, port, magazzino): 
        self.ip = ip
        self.port = port 
        self.magazzino = magazzino
   
    def preleva(self, articolo):
        return self.magazzino.preleva(articolo)

    def deposita(self, articolo, id): 
        return self.magazzino.deposita(articolo, id)

    def run_skel(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.ip, self.port))

        s.listen(30)
        print("Ready on port: ", s.getsockname()[1])
        i = 0

        while True: 

            c, addr = s.accept()
            i = i+1

            t = threading.Thread(target = thd_func, args = (c,self))
            t.start()

        sock.close()