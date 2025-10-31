from i_magazzino import IMagazzino
import multiprocess as mp 
from threading import Lock, Condition

class IMagazzinoImpl(IMagazzino): 

    def __init__ (self, host, port, queue_size):
        self.host = host
        self.port = port

        self.laptop_queue = []
        self.smartphone_queue = []

        laptop_lock = Lock()
        smartphone_lock = Lock()

        self.laptop_prod_cv = Condition(lock = laptop_lock)
        self.laptop_cons_cv = Condition(lock = laptop_lock)

        self.smartphone_prod_cv = Condition(lock = smartphone_lock)
        self.smartphone_cons_cv = Condition(lock = smartphone_lock)

        self.queue_size = queue_size 

        ##cosa intelligente da fare e aprire i file e pulirli cosi che siano
        ##vuoti ogni volta che il server viene avviato per evitare di contenere
        ##dati di esecuzioni precedenti ad esempio con truncate o open con "w".close

    
    def an_item_is_available(self, queue): 
        return not(len(queue) == 0)

    def a_space_is_available(self, queue): 
        return not(len(queue) == self.queue_size)

    def deposita(self, articolo, id): 
        success = True
        if articolo == "laptop":

            with self.laptop_prod_cv: 

                while not self.a_space_is_available(self.laptop_queue):
                    self.laptop_prod_cv.wait()
                
                id_item = self.laptop_queue.append(id)

                self.laptop_cons_cv.notify()

        elif articolo == "smartphone": 

            with self.smartphone_prod_cv: 

                while not self.a_space_is_available(self.smartphone_queue): 
                    self.smartphone_prod_cv.wait()

                id_item = self.smartphone_queue.append(id)

                self.smartphone_cons_cv.notify()

        else: 
            print("[MAGAZZZINO IMPL] Articolo non riconosciuto")
            success = False

        return success
        

    def preleva(self, articolo): 

        if articolo == "laptop": 

            with self.laptop_cons_cv: 

                while not self.an_item_is_available(self.laptop_queue): 
                    self.laptop_cons_cv.wait()
                
                id_item = self.laptop_queue.pop(0)

                with open("laptop.txt", 'a') as file: 
                    file.write(str(id_item) + "\n")

                self.laptop_prod_cv.notify()

        elif articolo == "smartphone": 

            with self.smartphone_cons_cv: 

                while not self.an_item_is_available(self.smartphone_queue): 
                    self.smartphone_cons_cv.wait()
                
                id_item = self.smartphone_queue.pop(0)

                with open("smartphone.txt", 'a') as file: 
                    file.write(str(id_item) + "\n")

                self.smartphone_prod_cv.notify()

        else: 
            print("[MAGAZZINO IMPL] Articolo non riconosciuto")
            id_item = -1

        return id_item