from magazzinoImpl import MagazzinoImpl


if __name__ == "__main__": 

    PORT = 0
    IP = "localhost"
    QUEUE_SIZE = 5
    
    magazzino = MagazzinoImpl(IP, PORT, QUEUE_SIZE)
    magazzino.run_skel()

    print("[MAGAZZINO] avviato...")
