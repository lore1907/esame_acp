import stomp, sys, time

class BWListener(stomp.ConnectionListener): 

    def __init__(self, filtro):
        self.filtro = filtro

    def on_error(self, frame): 
        print(f"[BW] Errore ricevuto: {frame.body}")

    def on_message(self, frame): 
        messaggio = frame.body
        print(f"[BW] Ricevuto Messaggio: {messaggio}")

        if self.filtro in messaggio:
            print(f" -> [MATCH] Il messaggio contiene '{self.filtro}'. Stampo!")

            with open("bw.txt", 'a') as f: 
                f.write(messaggio + "\n")
        else: 
            print(f" -> [SKIP] Il messaggio non contiene '{self.filtro}'. Ignoro")



if __name__ == "__main__": 

    if len(sys.argv) < 2:
        print("Uso: python bw_printer.py [bw|gs]")
        sys.exit(1)

    filtro_input = sys.argv[1]

    conn = stomp.Connection([("127.0.0.1", 61613)])

    conn.set_listener('', BWListener(filtro_input))
    conn.connect(wait=True)
    conn.subscribe(destination="/queue/bw", id =1, ack='auto')

    print(f"[BW PRINTER] In ascolto su /queue/bw con filtro: '{filtro_input}' ...")

    while True:
        time.sleep(1)