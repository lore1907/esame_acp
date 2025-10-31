import random, time 

from proxy import PrinterServerProxy

if __name__ == "__main__": 
    
    HOST = "localhost"
    PORT = 8000

    proxy = PrinterServerProxy(HOST, PORT)

    tipi_stampa = ["bw", "gs", "color"]
    estensioni = ["doc", "txt"]

    print("\n[USER] Avvio invio di 10 richieste...")
    for i in range(10): 
        num_file = random.randint(0,100)
        tipo_scelto = random.choice(tipi_stampa)
        estensione_scelta = random.choice(estensioni)

        path_file = f"/user/file_{num_file}.{estensione_scelta}"

        proxy.printReq(path_file, tipo_scelto)

        time.sleep(1)

    print("\n[USER] Tutte le richieste sono state inviate")
