import multiprocess as mp 
import random, time

NUM_MESS = 10

class Generator(mp.Process): 

    def __init__(self, queue_gen, name): 
        super().__init__(name = name)
        self.queue_gen = queue_gen


    def run(self): 

        typ = ["INFO" , "ERROR"]

        for i in range(NUM_MESS):

            chosen_type = random.choice(typ)
            num = random.randint(100, 999)
            message = f"{chosen_type}-{num}-{self.name}"
            time.sleep(random.uniform(0.05, 0.2))
            self.queue_gen.put(message)

        self.queue_gen.put(None)

