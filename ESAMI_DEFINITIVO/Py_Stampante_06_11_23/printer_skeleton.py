import socket
from i_printer import IPrinter
import threading as td
from abc import ABC, abstractmethod

def thd_fun(conn, skel): 

    mess = conn.recv(1024).decode("utf-8")
    parts = mess.split('-')
    if len(parts)  != 2: 
        return
    print(f"[SKELETON] Ricevuto {mess}")
    
    try:   
        pathFile, tipo = parts
        skel.print(pathFile, tipo)
    except Exception as e:
        print(f"Ricevuto errore {e}")
    finally:
        conn.close()


class PrinterSkeleton(IPrinter, ABC):

    def __init__(self, host, port): 
        self.host = host
        self.port = port

    def run_skel(self): 
        
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.host, self.port))

        print(f"Server connesso a {self.host}:{self.port}")
        s.listen(30)
        print("Socket in ascolto...")

        try:
            while (True): 
                c, add = s.accept()
                print("Connesso a: ", add[0], ":", add[1])

                t = td.Thread(target=thd_fun, args=(c, self))
                t.start()
        except KeyboardInterrupt: 
            print("Chiusura richiesta....")
        finally:   
            s.close()

    @abstractmethod
    def print(self, pathFile, tipo):
        raise NotImplementedError
