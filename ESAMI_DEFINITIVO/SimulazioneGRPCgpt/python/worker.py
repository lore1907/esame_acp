import stomp
import requests
import time


SERVER_FLASK = "http://127.0.0.1:5000"

class myListener(stomp.ConnectionListener):

    def __init__(self):
        self.stopped = False


    def on_message(self, frame):

        print(f"Ricevuto messaggio {frame.body}")
        msg = frame.body.strip()
        

        if msg == "STOP":
            self.stopped = True
            print("Richiedo stats...")
            endpoint = SERVER_FLASK + "/stats"
            response = requests.get(endpoint)


            try:
                response.raise_for_status()
            except requests.exceptions.HTTPError:
                print(f"Errore ricevuto {requests.status_codes} - {requests.text}")
            else: 
                print(f"STATS: {response}")

            return 

        parts = msg.split("-")
        if (len(parts) != 3):
            return 

        user = parts[0]
        people = int(parts[1])
        timeSlot = parts[2]

        msg = {"user": user, "people": people, "timeSlot":timeSlot}

        endpoint = SERVER_FLASK + "/reservations"
        response = requests.post(endpoint, json=msg)



if __name__ == "__main__":

    conn = stomp.Connection([("127.0.0.1", 61613)])
    listener = myListener()

    conn.set_listener('', listener)
    conn.connect(wait=True)
    conn.subscribe("/queue/booking")

    while not listener.stopped: 
        time.sleep(1)

    conn.disconnect()

        