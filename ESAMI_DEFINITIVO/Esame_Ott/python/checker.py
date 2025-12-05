import stomp 
import threading
import time

storico_temperature = []
storico_lock = threading.Lock()

class MyListener(stomp.ConnectionListener): 

    def on_error(self, frame):
        print(f"Errore nella ricezione del messaggio {frame.body}")

    def on_message(self, frame): 
        
        msg = frame.body
        parts = msg.split(('-'))
        
        if len(parts) != 2: 
            print(f"Errore formato messaggio, ricevuto: {msg}")
            return 

        value = parts[0]
        severity = parts[1]
        print(f"Messaggio ricevuto: {msg} con severity: {severity}")

        if severity.lower() == "2": 
            try:
                with open("errors.txt", "a") as f: 
                    f.write(msg + "\n")
            except Exception as e:
                print(f"Errore scrittura file: {e}")
                
        with storico_lock:
            try:
                storico_temperature.append(float(value))
            except ValueError as e: 
                print(f"Errore nel parsing del float {e}")

        print_stats()
    
def start_connection(i, severity):

    try: 

        conn = stomp.Connection([("127.0.0.1", 61613)])
        conn.set_listener(f"{severity} Listener",MyListener())
        conn.connect(wait=True)
        conn.subscribe(destination=f'/queue/{severity}', id=i, ack='auto')

        return conn
    
    except Exception as e: 
        print(f"Errore connessione {severity}: {e}")
        return None
    
def print_stats(): 

    with storico_lock:

        if not storico_temperature:
            return

        minimo = min(storico_temperature)
        massimo = max(storico_temperature)
        media = sum(storico_temperature)/len(storico_temperature)

    print("-----REPORT-----")
    print("\n")
    print(f"MINIMO : {minimo} \n")
    print(f"MASSIMO : {massimo} \n")
    print(f"MEDIA : {media} \n")    
    print("-----------------")


if __name__ == "__main__":

    severties = ["low", "mid", "high"]
    connections = []
    i = 0
    for severity in severties: 
        i+=1
        c = start_connection(i, severity)
        if c:
            connections.append(c)

    try:
        while True:
            time.sleep(1)

    except KeyboardInterrupt: 
        print("\nChiusura richiesta...")
        for c in connections:
            c.disconnect()
        print("Disconnesso.")

    