import threading, socket
from interfaces import IPrinter
from abc import ABC, abstractmethod 


file_lock = threading.Lock()

class PrinterSkeleton(IPrinter):

    def __init__(self, host, port):
        
        self.host = host
        self.port = port 

    @abstractmethod
    def print(self, pathFile, tipo):
        pass
    
    def run_skel(self): 

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: 

            s.bind((self.host, self.port))
            s.listen()
            print(f"[SKELETON P] in ascolto su {self.host}:{self.port}")

            while True: 
                c, addr = s.accept()
                threading.Thread(target=self._handler, args = (c, addr)).start()

    def _handler(self, conn, addr): 
        
        with conn: 
            data = conn.recv(1024).decode("utf-8")
            path_file, tipo = data.split("-")
            print(f"[PRINTER_HANDLER] Ricevuta richiesta da {addr}: print('{path_file}', '{tipo}')")
            self.print(path_file, tipo)


class PrinterImpl(PrinterSkeleton):

    def print(self, pathFile, tipo): 

        with file_lock: 
            with open(f"{tipo}.txt", 'a') as f: 
                log_message = f"Stampa del file: {pathFile} \t {tipo}\n"
                f.write(log_message)
                print(f"[PRINTER_IMPL] >> Scritto su {tipo}.txt: {log_message.strip()}")


if __name__ == "__main__": 
    HOST = "localhost"
    PORT = 8001

    printer = PrinterImpl(HOST, PORT)
    printer.run_skel()