from iMagazzino import IMagazzino 
import socket
from abc import ABC, abstractmethod
from skeletonThread import SkeletonThread

class SkeletonMagazzino(IMagazzino, ABC): 

    def __init__(self, ip, port): 
        self.ip = ip 
        self.port = port
        self.buf_size = 1024

    @abstractmethod 
    def deposita(self, articolo, id):
        raise NotImplementedError

    @abstractmethod
    def preleva(self, articolo): 
        raise NotImplementedError

    def run_skel(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.ip, self.port))

        porta = s.getsockname()[1]
        print(f"[SKELETON MAGAZZINO] connesso alla porta {str(porta)}")


        s.listen(30)

        i = 0
        while True: 

            c, addr = s.accept()
            i += 1
            print("[SKELETON MAGAZZINO] connesso a: ", addr[0], ":", addr[1])

            t = SkeletonThread("SKELETON THREAD-"+str(i), self.buf_size, c, self)
            t.start()

        s.close()

