import stomp, time, json 
import requests



FLASK_URL = "http://127.0.0.1:5000"


class BookingListener(stomp.ConnectionListener):

    def __init__(self, conn):
        self.conn = conn
        self.stopped = False


    def on_error(self, frame):
        print(f"[WORKER] Errore STOMP:  {frame.body}")

    def on_message(self, frame):
        body = frame.body.strip()

        print(f"[WORKER] Ricevuto messaggio da queue.booking : {body}")

        if body == "STOP":
            print("[WORKER] Ricevuto STOP. Richiedo statistiche..")

            response = requests.get(f"{FLASK_URL}/stats")

            try:
                response.raise_for_status()
            except requests.exceptions.HTTPError as e: 
                print(f"Error: Received {response.status_code} - {response.text}")
            else:
                stats = response.json()
                for slot, count in stats.items():
                    prinf(f"Fascia {slot}: {count} prenotazioni")

            self.stopped = True
            self.conn.disconnect()
            return
        
        try: 
            user, people_str, time_slot = body.split("-")
            people = int(people_str)
        except ValueError: 
            print("Formato messaggio non valido")
            return
        
        response = requests.post(f"{FLASK_URL}/reservations", json = payload)

        try:
            response.raise_for_status()
        except requests.exceptions.HTTPError as e: 
            print(f"Error: Received {response.status_code} - {response.text}")
        else:
            print(f"Prenotazione inviata con successo")

if __name__ == "__main__":

    conn = stomp.Connection([("127.0.0.1", 61613)])
    listener = BookingListener(conn)
    conn.set_listener("booking listener", listener)
    conn.connect(wait=True)

    print("[WORKER] Connesso a STOMP, sottoscrivo...")
    conn.subscribe(destination="/queue/booking", id ="bsub", ack='auto')

    try:
        while not listener.stopped:
            time.sleep(1)
    except KeyboardInterrupt as e:
        print(f"Richiesta chiusura -  {e}")
        conn.disconnect()