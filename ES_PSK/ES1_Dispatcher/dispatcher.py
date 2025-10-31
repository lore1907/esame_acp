from dispatcher_skel import DispatcherSkeleton 
from service_impl import ServiceImpl
import sys


if __name__ == "__main__":

    try: 
        HOST = sys.argv[1]
        PORT = sys.argv[2]
    except IndexError: 
        print("Errore")
        sys.exit(-1)

    
    serviceImpl = ServiceImpl()
    dispatcherSkel = DispatcherSkeleton(HOST, int(PORT), serviceImpl)
    dispatcherSkel.run_skel()

