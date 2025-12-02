import socket, sys, time, random 
from iPrinter import IPrinter

BUFFER_SIZE = 1024

class PrinterProxy(IPrinter):

    def __init__(self, host, port): 
        self.host = host
        self.port = port

    def print(self, pathFile, tipo): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        
        try: 

            s.connect((self.host, int(self.port)))

            request = f"{pathFile}-{tipo}"
            s.send(request.encode("utf-8"))
            print(f"[PROXY] Inviata richiesta: {request}")

        except Exception as e: 
            print(f"[PROXY] Errore di comunicazione {e}")

        finally: 
            s.close()

    