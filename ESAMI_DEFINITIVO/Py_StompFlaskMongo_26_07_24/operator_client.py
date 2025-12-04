import stomp , time
import threading
import random
import json, sys

NUM_THREADS = 6
HOTELS = ["Vesuvio", "GrandHotel", "MiraMare", "PortoFinoHotels", "LeHotel"]


class myListener(stomp.ConnectionListener): 

    def on_message(self, frame): 
        print(f"\n[OPERATOR] Ricevuto {frame.body}")



def thd_func(conn, i, operator): 

    if i <= 4: 
        tipo_richiesta = "CREATE"
        nights_number = random.randint(1,7)
        corpo_richiesta = {
            "client" : f"client_{random.randint(1,100)}", 
            "hotel" : f"{random.choice(HOTELS)}", 
            "operator" : operator,
            "nights" : nights_number, 
            "people" : random.randint(1,3),
            "cost" : str(random.random()*80*nights_number)
        }

        richiesta = tipo_richiesta + "-" + json.dumps(corpo_richiesta)
    else:
        tipo_richiesta = "UPDATE"
        nights_number = random.randint(1,7)
        corpo_richiesta = {
            "operator" : operator, 
            "nights" : nights_number, 
            "discount" : random.randrange(10, 50)
        }
        richiesta = tipo_richiesta + "-" + json.dumps(corpo_richiesta)

    try:
        conn.send("/topic/request", richiesta)
    except Exception as e:
        print(f"Errore nell'invio della richiesta {e}")
    


if __name__ == "__main__": 


    print("Inserire UserName!")
    try:
        operator = sys.argv[1]
    except IndexError: 
        print("Si prega di inserire l'operatore")
        sys.exit(1)

    conn = stomp.Connection([("127.0.0.1", 61613)])
    conn.set_listener('', myListener())
    conn.connect(wait=True)

    conn.subscribe(destination="/topic/response", id=1, ack='auto')
    print(f"Benvenuto operatore {operator}. Genero richieste...")
    threads = []

    i = 1
    for i in range(NUM_THREADS):
        i+=1
        t = threading.Thread(target=thd_func, args=(conn, i, operator))
        t.start()
        threads.append(t)

    for thread in threads: 
        thread.join()

    try:
        while(True):
            time.sleep(1)
    except KeyboardInterrupt: 
        print("\nRichiesta chiusura da tastiera...")
    finally: 
        conn.disconnect()