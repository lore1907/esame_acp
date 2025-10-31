from log_manager import LogManager
from generator import Generator
from file_writer import FileWriter
import sys
import multiprocess as mp 

if __name__ == "__main__": 

    q_gen = mp.Queue()
    q_fw = mp.Queue()

    fileWriter = FileWriter(q_fw, "fw")

    gen1 = Generator(q_gen, "gen1")
    gen2 = Generator(q_gen, "gen2")

    fileWriter.start()

    try:
        HOST = sys.argv[1]
        PORT = sys.argv[2]
    except IndexError:
        print("Please, specify HOST/PORT args...")
        sys.exit(-1)


    logManager = LogManager(q_gen, q_fw, HOST, int(PORT))
    logManager.start()
   
    gen1.start()
    gen2.start()


    # join dei processi 
    gen1.join()
    gen2.join()
    logManager.join()
    fileWriter.join()
