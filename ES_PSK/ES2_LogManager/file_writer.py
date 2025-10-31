import multiprocess as mp 
import time
class FileWriter(mp.Process): 

    def __init__(self, queue_fw, name): 
        super().__init__(name = name)
        self.queue_fw = queue_fw

    def run(self): 
        
        while True: 
            message = self.queue_fw.get()
            if message is None: 
                break 

            with open("events.log", mode="a") as eventsLog: 

                eventsLog.write(f"[{time.ctime()}] {message}\n")

        


