import time, random, sys
from printerProxy import PrinterProxy

def genera_richiesta(): 

    tipi = ["bw", "gs", "color"]
    estensioni = ["doc", "txt"]

    tipo_scelto = random.choice(tipi)
    num = random.randint(0, 100)
    estensione_scelta = random.choice(estensioni)

    path_file = f"/user/file_{num}.{estensione_scelta}"

    return path_file, tipo_scelto


if __name__ == "__main__": 


    HOST = sys.argv[1]
    PORT = int(sys.argv[2])

    proxy = PrinterProxy(HOST, PORT)

    print("[CLIENT] Avvio invio 10 richieste...")

    for i in range(10): 
        path, tipo = genera_richiesta()

        proxy.print(path, tipo)
        time.sleep(1)

    print("[CLIENT] Terminato. ")