import stomp
import multiprocess as mp 
import time

TOPICS = ["tickets", "stats"]

def produttore(coda, message): 
    coda.put(message)
    print(f"[TICKETS-PRODUTTORE]' Messaggio: {message} ' inserito in coda.")

def consumatore(coda): 
    stats_dict = {}

    artists = ["Jovanotti", "Ligabue", "Negramaro"]

    for artist in artists: 
        stats_dict[artist] = 0
        
    while not coda.empty(): 
        try:
            artist = coda.get_nowait()
            if artist in artists:
                stats_dict[artist] += 1
            else:
                print(f"Artista sconosciuto trovato in coda: {artist}. Scarto...")
        except:
            break
        
    try: 
        with open("stats.txt", "a") as f: 
            for artista, biglietti_venduti in stats_dict.items():
                line = f"{artista}-{biglietti_venduti}\n"
                f.write(line)
                print(f"[STATS] Scritto su file: {line.strip()}")
    except Exception as e: 
        print(f"Errore nella scrittura sul file: {e}")
        

def start_conn(i, topic, coda): 

    
    try:
        conn = stomp.Connection([("127.0.0.1", 61613)])
        conn.set_listener(f"{topic} Listener", myListener(topic, coda))
        conn.connect(wait=True)
        conn.subscribe(destination=f"/topic/{topic}", id = i, ack='auto')

        return conn
    except Exception as e: 
        print(f"Errore nella connessione stomp {e}")
        return None

class myListener(stomp.ConnectionListener): 

    def __init__(self, topic, coda=mp.Queue()): 
        self.topic = topic
        self.coda = coda

    def on_error(self, frame):
        print(f"Errore ricezione, messaggio ricevuto {frame.body}")

    def on_message(self, frame): 
        print(f"Ricevuto messaggio {frame.body} sul topic {self.topic}")
        message = frame.body

        if self.topic == "tickets": 
            p = mp.Process(target=produttore, args = (self.coda, message))
            p.start()
        elif self.topic == "stats":
            if message.upper() == "SOLD":
                p = mp.Process(target=consumatore, args = (self.coda,))
                p.start()



if __name__ == "__main__": 

    q = mp.Queue()
    i = 1
    conns = []
    for topic in TOPICS: 
        i+=1
        conn = start_conn(i, topic, q)
        if conn: 
            conns.append[conn]

    try:
        while(True):
            time.sleep(1)
            
    except KeyboardInterrupt: 
        for conn in conns: 
            try: 
                conn.close()
            except Exception as e: 
                print(f"Errore nella chiusura della connesione: {e}")