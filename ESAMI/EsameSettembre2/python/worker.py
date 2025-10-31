import stomp, logging
from multiprocess import Process, Queue



logging.basicConfig(level=logging.DEBUG, format="%(asctime)s - %(levelname)s - %(message)s")

class MyListener(stomp.ConnectionListener): 

    def __init__(self, queue, topic):
        self.queue = queue
        self.topic = topic

    def on_message(self, frame): 
        message = (frame.body or "").strip()
        logging.info(f"Ricevuto messaggio: ' {message} per il topic ' {self.topic} '")

        parts = message.split("-")
        req_type = parts[0].lower()

        if req_type == "deploy": 
            _id = parts[1]
            name = parts[0]
            task = {"id": _id, "name": name}

            try: 
                logging.info(f"[{self.topic}] Inserito {task} in coda")
                self.queue.put_nowait(task)
            except Exception:
                logging.error(f"[{self.topic}] Coda piena: task scartato")

        elif req_type == "stop_all":
            removed = 0
            while not self.queue.empty(): 
                elem = self.queue.get()
                removed += 1 
                logging.info(f"[{self.topic}] Rimosso elem: {elem}, \t\t n {removed}")

    def on_error(self, frame): 
        logging.error(f"Errore nella ricezione del messaggio: {frame.body}")


class WorkerProcess(Process):

    def __init__ (self, topic, queue, ip, port): 
        super().__init__
        self.topic = topic
        self.queue = queue
        self.ip = ip
        self.port = port

    def run (self):
        conn = stomp.Connection([(self.ip, self.port)])
        conn.set_listener(f'{self.topic} Listener', MyListener(self.queue, self.topic))
        conn.connect(wait=True)
        conn.subscribe(destination=f"/topic/{self.topic}", id=f"Sub {self.topic}", ack='auto')
        
        logging.info(f"[{self.topic}] Listener attivato su /topic/{self.topic}")

        try: 
            while True: 
                time.sleep(1)
        except KeyboardInterrupt: 
            logging.info("Richiesta interruzione da tastiera....")
        finally: 
            logging.info("Disconnessione in corso....")
            conn.disconnect()


class Worker: 

    def __init__(self): 
        self.queue = {"gpu": Queue(maxsize=5), "rt": Queue(maxsize=5)}

    def start(self, topic): 

        logging.info("Avvio processo per topic: " + str(topic))
        ip = "127.0.0.1"
        port = 61613
        p = WorkerProcess(topic, self.queue[topic], ip, port)
        p.start()
    

if __name__ == "__main__": 

    topics = ["gpu", "rt"]
    
    p = []
    for topic in topics: 
        worker = Worker()
        worker.start(topic)
        p.append()