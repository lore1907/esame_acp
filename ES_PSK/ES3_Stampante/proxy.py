import socket
from interfaces import IPrinterServer, IPrinter


class PrinterProxy(IPrinter): 

    def __init__(self, host, port):
        self.host = host
        self.port = port

    def print(self, pathFile, tipo): 

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: 
            
            try: 
                s.connect((self.host, self.port))
                message = f"{pathFile}-{tipo}"
                s.send(message.encode("utf-8"))

                print(f"[CONSUMER_PROXY] Richiesta print('{pathFile}', '{tipo}')")

            except Exception as e:
                print(f"[CONSUMER_PROXY] Errore durante la comunicazione: {e}")

    
class PrinterServerProxy(IPrinterServer): 


    def __init__(self, host, port):
        self.host = host
        self.port = port

    def printReq(self, pathFile, tipo): 

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: 

            try: 
                s.connect((self.host, self.port))
                message = f"{pathFile}-{tipo}"
                s.send(message.encode("utf-8"))

                print(f"[USER_PROXY] Richiesta inviata: {message}")

            except ConnectionRefusedError: 
                print("[USER_PROXY] Connection refused")
            except Exception as e:
                print(f"[USER_PROXY] Errore durante la comunicazione: {e}")