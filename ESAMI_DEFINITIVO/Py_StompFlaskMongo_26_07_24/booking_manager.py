import stomp 
import json
from flask import Flask
import requests
import time

server_address = "http://127.0.0.1:5000" 

class myListener (stomp.ConnectionListener): 

    def __init__(self, conn):
        self.conn = conn

    def on_message(self, frame): 
        print(f"[BOOKING MANGER] Messaggio ricevuto dall'operator : {frame.body}")

        parts = (frame.body).split("-")

        if len(parts) != 2: 
            print(f"Errore nel formato del messaggio,  ricevuto: [{frame.body}]")
            return 

        richiesta = parts[0]
        body = parts[1]

        if richiesta.upper() == "CREATE" or richiesta.upper() == "UPDATE": 
            print(f"Ricevuta richiesta {richiesta} per il messaggio {body}. Servo la richiesta...")
            payload = json.loads(body)
            endpoint = server_address + f"/database/{richiesta.lower()}"
            response = requests.post(endpoint, json = payload) if richiesta.upper() == "CREATE" else requests.put(endpoint, json=payload)

        else: 
            print(f"Tipo richiesta non riconosciuto per il messaggio. Ricevuto {richiesta} : {body}")
            return 

        try:
            response.raise_for_status()
            self.conn.send("/topic/response", response.text)
        except requests.exceptions.HTTPError as e: 
            print(f"[BOOKING MANAGER] Ricevuto errore {response.status_code} - {response.text}")
        else: 
            print(f"[BOOKING MANAGER] Richiesta: {richiesta} servita con successo")

        
if __name__ == "__main__": 

    conn = stomp.Connection([("127.0.0.1", 61613)])
    conn.set_listener('', myListener(conn))
    conn.connect(wait=True)

    conn.subscribe(destination="/topic/request", id=1, ack='auto')
    print("[BOOKING MANAGER] In attesa di richieste....")

    while (True): 
        time.sleep(60)