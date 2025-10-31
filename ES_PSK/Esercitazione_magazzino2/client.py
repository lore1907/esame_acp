from threading import Thread, current_thread 
from proxy import Proxy
import sys, random, time 

NUM_THREAD = 5
NUM_REQ = 3


def thd_fun(richiesta, port, ip, num_req): 
    proxy = Proxy(ip, port)

    for i in range(num_req): 
        t = random.randint(2,4)
        time.sleep(t)

        articolo = random.choice(["laptop", "smartphone"])
        result = None

        if richiesta == "deposita": 
            id_articolo = random.randint(1, 100)
            print(f"[{current_thread().name}] Richiesta {i+1}/{NUM_REQ}: deposita {articolo} con id {id_articolo}")
            result = proxy.deposita(articolo, id_articolo)

            if result: 
                print(f"[{current_thread().name}] -> Successo\n")
            else: 
                print(f"[{current_thread().name}] -> Fallimento\n")

        elif richiesta == "preleva": 
            print(f"[{current_thread().name}] Richiesta {i+1}/{NUM_REQ}: deposita {articolo}")
            result = proxy.preleva(articolo)

            if result != -1: 
                print(f"[{current_thread().name}] -> Successo. Prelevato ID: {result}\n")
            else: 
                print(f"[{current_thread().name} -> Fallimento\n]")
        
        else: 
            print("Richiesta non riconosciuta")
            

if __name__ == "__main__": 

    ip = "localhost"
    try:
        richiesta = sys.argv[1]
        port = sys.argv[2]
    except IndexError: 
        print("Si prega di eseguire client.py deposita/preleva porta")
        sys.exit(-1)
    
    threads = []

    for i in range(NUM_THREAD): 

        t = Thread(target=thd_fun, args=(richiesta, int(port), ip, NUM_REQ), name=f"CLIENT THREAD-{i}")
        threads.append(t)
        t.start()

    for thread in threads:
        thread.join()



