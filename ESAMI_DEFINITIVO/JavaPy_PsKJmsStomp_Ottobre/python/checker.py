import stomp
from threading import Lock


queues = ["low", "mid", "high"]

storico_temperature = []

class CheckerListener(stomp.ConnectionListener):

    def __init__(self, topic, lock):
        self.topic = topic
        self.lock = lock

    def on_message(self, frame):
        msg = frame.body
        print(f"[{self.topic} Listener] Ricevuto messaggio: {msg}")

        if self.topic.lower() == "high":
            with open("errors.txt", "a") as f:
                f.write(msg + "\n")
        
        value = float(msg.split("-")[0])

        with self.lock:
            storico_temperature.append(value)

        self.report()
        

    def report(self):
        with self.lock:
            minimo = min(storico_temperature)
            massimo = max(storico_temperature)
            media = sum(storico_temperature)/len(storico_temperature)

        print("-----REPORT----")
        print(f"MIN: {minimo}\t MAX: {massimo}\t MEDIA: {media}")
        print("----------------\n")


def start_connection(topic, lock):
    
    
    try:
        conn = stomp.Connection([("127.0.0.1", 61613)])
        conn.set_listener(f"{topic} Listener", CheckerListener(topic, lock))
        conn.connect(wait=True)
        destination = f"/queue/{topic}"

        conn.subscribe(destination=destination, id=1, ack="auto")

        return conn
    except Exception as e:
        print(f"Errore nella connesione stomp {e}")
        return None


if __name__ == "__main__":
    lock = Lock()

    for topic in queues: 
        start_connection(topic, lock)