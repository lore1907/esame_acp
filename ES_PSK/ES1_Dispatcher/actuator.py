import sys, random, time
from datetime import datetime
from dispatcher_proxy import DispatcherProxy
import threading as mt



if __name__ == "__main__": 

    try: 
        HOST = sys.argv[1]
        PORT = sys.argv[2]
    except IndexError: 
        print("Error")
        sys.exit(-1)


    command_dict = {0:"leggi", 1:"scrivi", 2:"configura", 3:"reset"}
    i = 0 

    while True: 

        time.sleep(1)

        proxy = DispatcherProxy(HOST, int(PORT), mt.current_thread().name, i)
        data = proxy.getCmd()

        ## per evitare di aprire e chiudere il file ogni volta potrei passare l open fuori dal while 
        ## col while dentro che scrive finche non ha terminato e quindi trovare una cond di false
        print(f'[ACTUATOR] received data for #{i} request: {data}...write to file\n')

        with open("cmdLog.txt", mode='a') as log_file: 

            log_file.write(f"{datetime.now()} {command_dict[int(data)]} \n")

        i = i+1

    