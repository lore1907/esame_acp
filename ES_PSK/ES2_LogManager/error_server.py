from error_service_impl import ErrorServiceImpl
from skeleton import Skeleton
import sys, time, random 

if __name__ == "__main__": 

    try: 
        PORT = sys.argv[1]
    except IndexError: 
        print("Errore, inserire porta...")
        sys.exit(-1)

    serviceImpl = ErrorServiceImpl("localhost", int(PORT))
    skel = Skeleton("localhost", int(PORT), serviceImpl)
    skel.run_skel()
    



