from service import Service
import multiprocess as mp 

class ServiceImpl(Service): 

    def __init__(self, command_queue = mp.Queue(5)):
        self.command_queue = command_queue

    def getCmd(self): 
        value_to_return = self.command_queue.get()
        return value_to_return

    def sendCmd(self, value): 
        self.command_queue.put(value)
        