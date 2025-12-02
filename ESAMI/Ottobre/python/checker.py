import stomp 
import multiprocess as mp
import time

storico_temperature = []

class myListener(stomp.ConnectionListener): 

    def __init__(self, severity):
        self.severity = severity

    def on_error(self, frame): 
        print(f"Errore nella ricezione del messaggio {frame.body}")

    def on_message(self, frame): 
        
        msg = frame.body 
        
        try: 
                
            parts = msg.split('-')
            value = parts[0]
            severity_msg = parts[1]
            storico_temperature.append(float(value))
            
            print(f"[{self.severity}] Messaggio ricevuto {msg}")

            if severity_msg.lower() == "high": 
                with open("errors.txt", 'a') as f: 
                    f.write(msg + "\n")

            self.print_stats()

        except Exception as e: 
            print(f"Errore nel parsing del messaggio {e}")


    def print_stats(self): 
        if len(storico_temperature) > 0: 
            minimo = min(storico_temperature)
            massimo = max(storico_temperature)
            media = sum(storico_temperature)/len(storico_temperature)

            print("--- REPORT ---")
            print(f"Totale misurazioni: {len(storico_temperature)}")
            print(f"Min: {minimo}")
            print(f"Max: {massimo}")
            print(f"Media: {media}")
            print(f"--------------")

def start_listener(severity): 
    conn = stomp.Connection([("127.0.0.1", 61613)])
    conn.set_listener('', myListener(severity))
    conn.connect(wait=True)
    conn.subscribe(destination=f"/queue/{severity}", id=1, ack='auto')

    print(f"[{severity}] In ascolto...")
    while True:
        time.sleep(1)


if __name__ == "__main__": 

    serverities = ["low", "mid", "high"]
    processes = []

    for severity in serverities: 
      p = mp.Process(target=start_listener, args=(severity,))
      p.start()
      processes.append(p)
   
    for p in processes: 
        p.join()