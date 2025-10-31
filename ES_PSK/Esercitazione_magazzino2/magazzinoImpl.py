from skeletonMagazzino import SkeletonMagazzino
from threading import Lock, Condition

class MagazzinoImpl(SkeletonMagazzino): 

    def __init__(self, ip, port, queue_size=5): 

        super().__init__(ip, port)

        laptop_lock = Lock()
        smartphone_lock = Lock()

        self.lap_queue = []
        self.smrt_queue = []

        self.lap_p_cv = Condition(lock=laptop_lock)
        self.lap_c_cv = Condition(lock=laptop_lock)

        self.smrt_p_cv = Condition(lock=smartphone_lock)
        self.smrt_c_cv = Condition(lock=smartphone_lock)

        self.lap_file = "laptop.txt"
        self.smrt_file = "smartphone.txt"
        open(self.lap_file, 'w').close()
        open(self.smrt_file, 'w').close()

        self.queue_size = queue_size


    def _an_item_is_available(self, queue): 
        return not(len(queue) == 0)

    def _a_space_is_available(self, queue): 
        return not(len(queue) == self.queue_size)

    
    
    def preleva(self, articolo): 
        
        if articolo == "laptop": 

            with self.lap_c_cv: 
                while not self._an_item_is_available(self.lap_queue): 
                    self.lap_c_cv.wait()
                
                articolo_consumato = self.lap_queue.pop(0)
                self.lap_p_cv.notify()
        
        elif articolo == "smartphone": 

            with self.smrt_c_cv: 
                while not self._an_item_is_available(self.smrt_queue): 
                    self.smrt_c_cv.wait()
                
                articolo_consumato = self.smrt_queue.pop(0)
                self.smrt_p_cv.notify()
        
        else: 
            print(f"L'articolo {articolo} non esiste in catalogo")
            return -1

        return articolo_consumato
            
        

    def deposita(self, articolo, id): 
        
        success = True

        if articolo == "laptop": 

            with self.lap_p_cv: 
                while not self._a_space_is_available(self.lap_queue): 
                    print("Produttori laptop in attesa")
                    self.lap_p_cv.wait()

                return_id = self.lap_queue.append(id) 
                self.lap_c_cv.notify()

        elif articolo == "smartphone": 

            with self.smrt_p_cv: 
                while not self._a_space_is_available(self.smrt_queue): 
                    print("Produttori smartphone in attesa")
                    self.smrt_p_cv.wait()

                return_id = self.smrt_queue.append(id) 
                self.smrt_c_cv.notify()

        else: 
            print(f"Articolo {articolo} non riconosciuto")
            success = False

        return success
