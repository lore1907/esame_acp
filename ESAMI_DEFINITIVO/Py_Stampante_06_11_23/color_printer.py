import stomp 
import sys, time

class myListener(stomp.ConnectionListener): 

    def __init__(self, input_string):
        self.input_string = input_string

    def on_error(self, frame): 
        print(f"Errore, ricevuto: {frame.body}")

    def on_message(self, frame): 
        messaggio_ricevuto = frame.body

        if self.input_string in messaggio_ricevuto: 
            print(f"[COLOR PRINTER LISTENER] Ricevuto messaggio {messaggio_ricevuto}")
            with open("color.txt", "a") as f: 
                f.write(f"{messaggio_ricevuto} \n")
        else: 
            print(f"[COLOR PRINTER LISTENER] Il messaggio non contiene l'input desiderato, : {messaggio_ricevuto}")



if __name__ == "__main__": 

    try: 
        STRINGA = sys.argv[1]
    except IndexError:
        print("Uso: bw_printer.py <doc/txt> ")
        sys.exit(1)

    try: 
        conn = stomp.Connection([("127.0.0.1", 61613)])
        conn.set_listener(f"{STRINGA} Listener", myListener(STRINGA))
        conn.connect(wait=True)
        conn.subscribe(destination="/queue/color", id=1, ack="auto")
    except Exception as e:
        print(f"Errore di connessione stomp {e}")
   
    try:
        while(True):
            time.sleep(1)
    except KeyboardInterrupt: 
        print("Richiesta chiusura da tastiera...")
        conn.close()
    