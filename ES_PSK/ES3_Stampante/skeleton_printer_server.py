import socket, threading 
import multiprocess as mp 
from abc import ABC, abstractmethod 
from interfaces import IPrinterServer 
from proxy import PrinterProxy


class Consumer(mp.Process): 

    def __init__(self, queue, printer_host, printer_port):
        super().__init__()
        self.queue = queue 
        self.printer_proxy = PrinterProxy(printer_host, printer_port)
    

    def run(self): 
        print("[CONSUMER] Avviato")

        while True: 
            try: 
                data = self.queue.get()
                if data == "TERMINATE": break 

                print(f"[CONSUMER] Prelevata richiesta: {data}")
                pathFile, tipo = data.split("-")

                self.printer_proxy.print(pathFile, tipo)

            except Exception as e: 
                print(f"[CONSUMER] Errore : {e}")

            print("[CONSUMER] Terminato")

    
class PrinterServerSKeleton(IPrinterServer): 
    
    def __init__(self, host, port): 
        self.host = host 
        self.port = port 
        self.running = True

    @abstractmethod
    def printReq(self, pathFile, tipo): 
        pass

    def run_skel(self):

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s: 

            s.bind((self.host, self.port))
            s.listen()
            print(f"[SKELETON PS] in ascolto su {self.host}:{self.port}")

            while self.running: 
                c, addr = s.accept()
                threading.Thread(target=self._handler, args = (c, addr)).start()

    def _handler(self, conn, addr): 

        with conn: 
            data = conn.recv(1024).decode("utf-8")
            path_file, tipo = data.split("-")
            print(f"[PRINTER_SERVER_HANDLER] Ricevuta richiesta da {addr})")
            self.printReq(path_file, tipo)


class PrinterServerImpl(PrinterServerSKeleton): 

    def __init__(self, host, port, queue):
        super().__init__(host, port)
        self.queue = queue

    def printReq(self, pathFile, tipo): 
        data_to_queue = f"{pathFile}-{tipo}"
        self.queue.put(data_to_queue)
        print(f"[PRODUCER] Richiesta '{data_to_queue}' inserita in coda.")

if __name__ == "__main__": 
    PRINTER_SERVER_HOST = "localhost" 
    PRINTER_SERVER_PORT = 8000

    PRINTER_HOST =  "localhost"
    PRINTER_PORT = 8001

    request_queue = mp.Queue()

    consumer_process = Consumer(request_queue, PRINTER_HOST, PRINTER_PORT)
    consumer_process.start()

    printer_server = PrinterServerImpl(PRINTER_SERVER_HOST, PRINTER_SERVER_PORT, request_queue) 

    try: 
        printer_server.run_skel()
    except KeyboardInterrupt: 
        print("\n[PRINTER SERVER] Chiusura...")
        printer_server.running = False
        request_queue.put("TERMINATE")
        consumer_process.join()
        print("[PRINTER SERVER] Chiuso")