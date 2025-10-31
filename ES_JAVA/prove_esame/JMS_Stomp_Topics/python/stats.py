import stomp, logging, time
import multiprocess as mp 

from queue import Empty
from collections import Counter

logging.basicConfig(level=logging.DEBUG, filename="stats.log", format=("%(asctime)s - %(levelname)s - %(message)s"))

stop = mp.Event()

purchases_q = mp.Queue()
ARTISTI = ["LIGABUE", "JOVANOTTI", "NEGRAMARO"]


def producer(q, artist):
    if artist.upper() in ARTISTI: 
        logging.info(f"[Producer] Aggiungo '{artist}' alla coda")
        q.put(artist)
    else: 
        logging.warning("Artista sconosciuto: %s", artist)
        

def consumer(q): 
    
    logging.info("Ricevuto SOLD, log delle statistiche avviato...")

    counts = Counter()

    stats_dict = {}

    while True: 
        try: 
            a = q.get_nowait()
            counts[a] += 1
        except Empty:
            break

    with open("stats.txt", "w", encoding="utf-8") as f: 
        for artist, n in sorted(counts.items()): 
            stats_dict[artist] = n
            f.write(f"{artist}\t\t:\t\t{n}\n") 
    
    logging.info("Consumer: completato. Biglietti totali: %d", sum(counts.values()))



class myListener(stomp.ConnectionListener): 

    def __init__(self, conn): 
        self.conn = conn 

    def on_message(self, frame): 
        message = (frame.body or "").strip()
        dest = frame.headers.get("destination", "")
        logging.info("Ricevuto '%s' da '%s'", message, dest)
        
        #if dest == "/topic/tickets_shop)
        if dest.endswith("tickets_shop"):
            p = mp.Process(target=producer, args=(purchases_q, message))
            p.start()
            
        elif dest.endswith("tickets_stats"): 
            if message.upper() == "SOLD": 
                p = mp.Process(target=consumer, args=(purchases_q,))
                p.start()
            else: 
                logging.warning("Messaggio inatteso su stats: %s", message)

if __name__ == "__main__": 

    conn = stomp.Connection([("127.0.0.1", 61613)])
    conn.set_listener('', myListener(conn))
    conn.connect(wait=True)

    conn.subscribe(destination="/topic/tickets_shop", id = 1, ack='auto')
    conn.subscribe(destination="/topic/tickets_stats", id = 2, ack = 'auto')


    logging.info("Listener STOMP avviato.")

    try: 
        while True:
            time.sleep(1)
    except KeyboardInterrupt: 
        logging.info("Shutdown richiesto.")
    finally: 
        conn.disconnect()







    