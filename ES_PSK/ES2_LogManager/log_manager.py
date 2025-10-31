import multiprocess as mp 
from proxy import Proxy
import sys


class LogManager(mp.Process): 

    def __init__(self, queue_gen, queue_fw, host, port): 
        super().__init__()
        self.queue_gen = queue_gen
        self.queue_fw = queue_fw
        self.host = host
        self.port = port

    def run(self):
        print("[LOG MANAGER] running...") 

        stops = 0

        while True: 

            message = self.queue_gen.get()
            if message is None:
                stops+=1
                if stops == 2:
                    break
                continue
                

            print(f"[Log Manager] get: {message}")
            header = message.split("-")[0]

            proxy_to_server = Proxy(self.host, int(self.port))
            if header.upper() == "INFO": 
                self.queue_fw.put(message)
                print(f"[Log Manager] put: {message}")
            elif header.upper() == "ERROR": 
                self.queue_fw.put(message)
                proxy_to_server.writeError(message)
                print(f"[Log Manager] sended: {message}")

        self.queue_fw.put(None)

        return


