import stomp 
import requests 
import time


SERVER_ADDRESS = "http://127.0.0.1:5000"

class myListener(stomp.ConnectionListener):

    def __init__(self, conn):
        self.conn = conn
        self.stopped = False

    def on_error(self, frame):
        print(f"Errore ricevuto :{frame.body}")

    def on_message(self, frame):

        msg = frame.body.strip() #per rimuovere eventuali spazi
        
        
        if msg == "STOP":
            print(f"[STOP] Richieste terminate.. stampo le statistiche")
            response = requests.get(SERVER_ADDRESS+"/stats")

            try:
                response.raise_for_status()
            except requests.exceptions.HTTPError:
                print(f"Errore, ricevuto {response.status_code} - {response.text}")
            else:
                stats = response.json()
                print(f"STATS = \n {stats}")
            
            self.stopped = True

            return

        
        print(f"Ricevuto messaggio dal SENDER JMS, {msg}")
        user = msg.split("-")[0]
        people = int(msg.split('-')[1])
        timeSlot = msg.split("-")[2]

        json_msg = {"user":user, "people": people, "timeSlot": timeSlot}

        response = requests.post(SERVER_ADDRESS+"/reservations", json=json_msg)
        try:
            response.raise_for_status()
        except requests.exceptions.HTTPError:
            print(f"Errore, ricevuto {response.status_code} - {response.text}")
        else:
            result = response.json()
            print(f"Prenotazione salvata, Risultato : {result}")



if __name__ == "__main__":

    conn = None
    try:
        conn = stomp.Connection()
        listener = myListener(conn)
        conn.set_listener('booking Listener', listener)
        conn.connect(wait=True)
        conn.subscribe(destination="/queue/booking", id=1, ack='auto')

        while(not listener.stopped):
            time.sleep(1)

    except Exception as e:
        print(f"Errore connessione stomp, chiudo le risorse...")
    
    finally: 
        conn.disconnect()