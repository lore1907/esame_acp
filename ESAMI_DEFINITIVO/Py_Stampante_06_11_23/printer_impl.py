from printer_skeleton import PrinterSkeleton
import multiprocess as mp
import stomp

host = "127.0.0.1"
port = 61613

class PrinterImpl(PrinterSkeleton): 

    def __init__(self, host, port, queue): 
        super().__init__(host, port)
        self.queue = queue
        self.consumatore = mp.Process(target=consuma, args=(self.queue,))
        self.consumatore.start()

    def print(self, pathFile, tipo): 
        mess = f"{pathFile}-{tipo}" 
        produttore = mp.Process(target=produci, args=(self.queue, mess))
        produttore.start()

def produci(queue, mess): 
    queue.put(mess)
    print(f"[PRODUTTORE] Inserito {mess} in coda")

def consuma(queue):
    conn = stomp.Connection([(host, port)])
    
    try:
        conn.connect(wait=True)
    except Exception as e:
        print(f"[CONSUMER] Errore connessione stomp {e}")
        return

    while(True):
        mess = queue.get()
        print("[CONSUMER] Prelevato dalla coda" + mess)
        parts = mess.split("-")

        if len(parts) != 2:
            return

        pathFile, tipo = parts

        destination = "/queue/color" if tipo == "color" else "/queue/bw"
        conn.send(destination, mess)
        print(f"[CONSUMER] {mess} inviato a {destination}")

