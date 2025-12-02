import socket, sys, time 
from abc import ABC, abstractmethod
from iPrinter import IPrinter
import multiprocess as mp



def run_func(conn, skel): 

    try: 
        message = (conn.recv(1024)).decode('utf-8')
        if not message: return
        print("Message recieved: ", message)

        parts = message.split("-")
        if len(parts) == 2: 
            path_file = parts[0]
            tipo = parts[1]

            skel.print(path_file, tipo)
    
    except Exception as e: 
        print(f"Errore worker: {e}")
    finally:
        conn.close()

class PrinterServerSkeleton(IPrinter, ABC): 

    def __init__(self, host, port): 
        self.host = host 
        self.port = port
        

    @abstractmethod
    def print(self, pathFile, tipo): 
        pass 


    def run_skel(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.host, self.port))
        self.port = s.getsockname()[1]

        print("Socket binded to host: " + self.host + " and port: " + str(self.port))
        s.listen(30)
        print("Socket is listening...")

        while True: 
            c, addr = s.accept()
            print("Connected to :", addr[0], ":", addr[1])

            t = mp.Process(target=run_func, args = (c, self))
            t.start()
            print("[DISPATCHER SKELETON] t is teminated")

        s.close()
        print("[DISPATCHER SKELETON] socket is closed...")



