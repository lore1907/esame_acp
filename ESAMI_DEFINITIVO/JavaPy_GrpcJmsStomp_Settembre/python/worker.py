import stomp, sys
import multiprocess as mp
import time

## due code 1 per topic, ricezione asincrona sulla coda, alla ricezione leggo messaggio e verifico operazione
## deploy inserisce il mess in coda, stop svuota la coda relativa al proprio topoic e entrambi stampano a video

class myListener(stomp.ConnectionListener):

    def __init__(self, queue, topic):
        self.queue = queue
        self.topic = topic

    def on_error(self, frame):
        print(f"[LISTENER {self.topic}] Ricevuto messaggio con formato errato: {frame.body}")

    def on_message(self, frame): 
        msg = frame.body
        print(f"[LISTENER {self.topic}] Ricevuto {frame.body}")

        if msg == "stopAll":
            print(f"[{self.topic}] STOP RICEVUTO. Svuoto coda...")
            while not self.queue.empty():      
                try:
                    rimosso = self.queue.get_nowait()
                    print(f"[{self.topic}] Rimosso: {rimosso}")
                except:
                    pass
        elif "deploy" in msg:
            try: 
                self.queue.put_nowait(msg)
                print(f"[{self.topic}] Messaggio accodato: {msg}")
            except:
                print(f"[{self.topic}] Coda piena! Perso : {msg}")


class Worker(mp.Process):

    def __init__(self, topic, event):
        super().__init__()
        self.topic = topic
        self.stop_event = event
        self.queue = mp.Queue(5)

    def run(self):
        conn = None
        try:
            conn = stomp.Connection([("127.0.0.1", 61613)])
            conn.connect(wait=True)    
            conn.set_listener(f"{self.topic} Listener", myListener(self.queue, self.topic))
            destination = f"/queue/{self.topic}"
            conn.subscribe(destination=destination, id=f"sub {self.topic}", ack='auto')
            print(f"[WORKER {self.topic}] Connesso e in attesa su {destination}....")

            #bisogna mantenere attivo per ricevere tutti i messaggi
            while not self.stop_event.is_set():
                time.sleep(1)

        except Exception as e:
            print("Erorre nella connessione stomp")
        finally: 
            if conn:
                conn.disconnect()
                print(f"[WORKER {self.topic}] Disconnessione eseguita!")


if __name__ == "__main__": 
    
    topics = ["gpu", "rt"]
    workers = []
    stop_event = mp.Event()

    for topic in topics: 
            worker = Worker(topic, stop_event)
            worker.start()
            workers.append(worker)

    try:
        while True:
            time.sleep(1)
        
    except KeyboardInterrupt :
        print("Richiesta chiusura da tastiera...")
        stop_event.set()
        ## se facessi fuori il join dato che ho il while true nei processi bloccherei tutto quiindi li aspetto solo quando si e verificato l evento
        for worker in workers:
            worker.join()
        
        print("Sistema terminato correttamente...")

    

    

    