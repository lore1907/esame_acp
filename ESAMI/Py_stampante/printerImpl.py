from printer_skeleton import PrinterServerSkeleton
import multiprocess as mp 
from threading import Lock
import stomp 


class PrinterImpl(PrinterServerSkeleton):

    def __init__(self, host, port): 
        super().__init__(host, port)
        self.queue = mp.Queue()
        self.consumatore = mp.Process(target=processoConsumatore, args=(self.queue, ))
        self.consumatore.start()

    def print(self, pathFile, tipo): 
        print(f"[PRINTER IMPL] Richiesta ricevuta: {pathFile} ({tipo})")
        p = mp.Process(target=processoProduttore, args=(self.queue, pathFile,tipo))
        p.start()

def processoProduttore(coda, pathFile, tipo): 

    msg_to_print = f"{pathFile}-{tipo}"
    coda.put(msg_to_print)
    print(f"[PRODUCER] Inserito in coda interna: {msg_to_print}")

def processoConsumatore(coda): 
    conn = stomp.Connection([("127.0.0.1", 61613)])
    try: 
        conn.connect(wait=True)
        print("[CONSUMER] Connesso")
    except Exception as e: 
        print(f"[CONSUMER] Errore connessione stomp {e}")
        return

    while True: 
        messaggio_completo = coda.get()
        print(f"[CONSUMER] Prelevato dalla coda: {messaggio_completo}")

        try: 
            parts = messaggio_completo.split("-")
            path = parts[0]
            tipo = parts[1]

            destinazione = '/queue/color' if tipo == 'color' else '/queue/bw'

            conn.send(destinazione, messaggio_completo)
            print(f"[CONSUMER] Inviato a {destinazione}: {messaggio_completo}")

        except IndexError: 
            print("[CONSUMER] Formato messaggio errato")
