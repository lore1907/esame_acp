import time, sys, stomp 


class ColorListener(stomp.ConnectionListener): 
    def __init__(self, filtro): 
        self.filtro = filtro

    def on_error(self, frame): 
        print(f"[COLOR] Errore ricevuto: {frame.body}")

    def on_message(self, frame): 
        messaggio = frame.body
        print(f"[COLOR] Ricevuto Messaggio: {messaggio}")

        if self.filtro in messaggio:
            print(f" -> [MATCH] Il messaggio contiene '{self.filtro}'. Stampo!")

            with open("color.txt", 'a') as f: 
                f.write(messaggio + "\n")
        else: 
            print(f" -> [SKIP] Il messaggio non contiene '{self.filtro}'. Ignoro")



if __name__ == "__main__": 

    if len(sys.argv) < 2:
        print("Uso: python bw_printer.py [bw|gs]")
        sys.exit(1)

    filtro_input = sys.argv[1]

    conn = stomp.Connection([("127.0.0.1", 61613)])

    conn.set_listener('',  ColorListener(filtro_input))
    conn.connect(wait=True)
    conn.subscribe(destination="/queue/color", id =1, ack='auto')

    print(f"[COLOR PRINTER] In ascolto su /queue/color con filtro: '{filtro_input}' ...")

    while True:
        time.sleep(1)