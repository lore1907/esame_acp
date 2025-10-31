import sys, threading, random

from dispatcher_proxy import DispatcherProxy

N_CLIENTS = 5 
N_REQ = 3

def generate_requests(host, port):

    for i in range (N_REQ): 

        value_to_send = random.randint(0,3)

        thread_name = threading.current_thread().name

        proxy = DispatcherProxy(host, int(port), thread_name, i)
        proxy.sendCmd(value_to_send)

    
if __name__ == "__main__": 

    try: 
        HOST = sys.argv[1]
        PORT = sys.argv[2]
    except IndexError: 
        print("Error")
        sys.exit(-1)

    clients = [ ]

    for i in range (N_CLIENTS): 

        client = threading.Thread(target=generate_requests, args=(HOST, PORT))

        client.start()

        clients.append(client) 

    for i in range (N_CLIENTS): 
        clients[i].join()       