from services import Services
import multiprocess as mp 

class Implementations(Services): 

    def __init__(self, queue): 
        self.queue = queue

    def preleva(self):
        result = self.queue.get()
        return result

    def deposita(self, id_articolo):
        self.queue.put(id_articolo)
        
        