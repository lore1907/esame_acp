import time, sys
import random
from printer_proxy import PrinterProxy

supported_types = ["bw", "gs", "color"]
extensions = ["doc", "txt"]
NUM_REQ = 10
HOST = "localhost"

if __name__ == "__main__": 

    try: 
        PORT = int(sys.argv[1])
    except (IndexError, ValueError):
        print("Uso: user.py <porta>")
        sys.exit(1)

    string_path = "/user/file_"
    proxy = PrinterProxy(HOST, PORT)

    for i in range(NUM_REQ):
        tipo = random.choice(supported_types)
        estensione = random.choice(extensions)
        num = random.randint(0, 100)

        pathFile = string_path + f"{num}.{estensione}"
        proxy.print(pathFile, tipo)
        time.sleep(1)
