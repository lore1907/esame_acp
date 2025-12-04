from i_printer import IPrinter
import socket

class PrinterProxy(IPrinter): 

    def __init__(self, host, port):
        self.host = host 
        self.port = port

    def print(self, pathFile, tipo): 
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        
        try:
            s.connect((self.host, self.port))

            msg = f"{pathFile}-{tipo}"
            msg_to_send = msg.encode("utf-8")

            s.send(msg_to_send)
            print(f"[PROXY] Inviata richiesta: {msg_to_send}")
        
        except Exception as e:
            print(f"[PROXY] Errore di comunicazione {e}")
        finally:
            s.close()