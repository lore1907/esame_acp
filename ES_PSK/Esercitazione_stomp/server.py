from skeleton import Skeleton 
from implementations import Implementations
import multiprocess as mp 

if __name__ =="__main__": 

    PORT = 8001
    IP = "localhost"
    queue = mp.Queue()

    impl = Implementations(queue)
    skel = Skeleton(IP, PORT, impl)
    skel.run_skel()
    
