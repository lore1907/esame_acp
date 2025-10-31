from threading import Thread, current_thread 
from proxy import Proxy 

import sys, random , time

def thread_fun(service, ip, port, num_req): 

    waiting_time = random.randint(2,4)
    time.sleep(waiting_time)
    
    proxy = Proxy(ip, int(port))

    for i in range(num_req): 

        choice = random.randint(0,1)

        if choice == 0 : 
            articolo = "smartphone"
        else: 
            articolo = "laptop"

        if service == "deposita": 
            id = random.randint(1,100)
            result = proxy.deposita(articolo, id)

            if not result: 
                print("request failed")
            else: 
                print("request succeded")

        elif service == "preleva": 
            result = proxy.preleva(articolo)

            if result == -1 :
                print("Request failed")
            else: 
                print("Response succeded")


NUM_THREAD = 5
NUM_REQ_T = 3
IP = "localhost"

try: 
    service = sys.argv[1]
    port = sys.argv[2]
except IndexError: 
    print("Errrore")
    sys.exit(-1)

threads = []


for i in range(NUM_THREAD): 
    t = Thread(target = thread_fun, args = (service, IP, port, NUM_REQ_T), name="CLIENT THREAD-"+str(i))
    threads.append(t)
    t.start()

for i in range(NUM_THREAD):
    threads.pop().join()


