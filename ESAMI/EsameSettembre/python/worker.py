import stomp 
from multiprocess import Process, Queue
import logging, time, sys


logging.basicConfig(
        level=logging.INFO, 
        format = ("%(asctime)s - %(levelname)s - %(message)s"), 
        handlers=[
            logging.FileHandler("worker.log"),
            logging.StreamHandler(sys.stdout)
        ]
    )


class MyListener(stomp.ConnectionListener):

    def __init__(self, topic, q): 
        self.q = q
        self.topic = topic

    def on_error(self, frame):
        logging.error(f"[{self.topic}] STOMP ERROR: {frame.body}")

    def on_message(self, frame):
        msg = frame.body.strip()
        logging.info(f"[{self.topic}] Ricevuto: {msg}")
        
        parts = msg.split("-")
        cmd = parts[0].lower()


        if cmd == "deploy": 
            if len(parts) < 3: 
                logging.warning(f"[{self.topic}] deploy malformato: {msg}")
                return
            payload = f"{parts[1]}-{parts[2]}"

            try: 
                self.q.put_nowait(payload)
                logging.info(f"[{self.topic}] Inserito in coda: '{payload}'")

            except Exception:
                logging.warning(f"[{self.topic}] Coda piena (size=5). \n Drop di '{payload}'")

        elif cmd == "stop_all": 
            removed = 0
            while not self.q.empty():
                element = self.q.get()
                removed +=1
                logging.info(f"[{self.topic}] Rimosso : {element} n {removed}")

            logging.info(f"[{self.topic}] Coda svuotata completamente.")
        
        else: 
            logging.warning(f"[{self.topic}] Comando sconosciuto> {cmd}")


    
class Worker(Process): 

    def __init__(self, topic): 
        super().__init__(self)
        self.topic = topic
        self.queue = Queue(maxsize=5)

    def run(self): 
        conn = stomp.Connection([("127.0.0.1", 61613)])
        listener = MyListener(self.topic, self.queue)
        conn.set_listener(self.topic, listener)

        while True: 
            
            try: 
                conn.connect(wait=True)
                conn.subscribe(destination=f"/topic/{self.topic}", id = self.topic , ack = "auto")
                logging.info(f"[{self.topic}] Worker avviato. In ascolto su /topic/{self.topic}") 
                break 
            except Exception as e: 
                logging.error(f"[{self.topic}] Connessione fallita: {e}")
                time.sleep(2)

        try: 
            while True:
                time.sleep(1)
        except KeyboardInterrupt:
            logging.info(f"[{self.topic}] Terminazione richiesta.")
        finally:
            conn.disconnect()
            return


if __name__ == "__main__" :

    gpu_worker = Worker("gpu")
    rt_worker = Worker("rt")

    gpu_worker.start()
    rt_worker.start()

    logging.info('Worker per GPU e RT avviati')
    gpu_worker.join()
    rt_worker.join()

