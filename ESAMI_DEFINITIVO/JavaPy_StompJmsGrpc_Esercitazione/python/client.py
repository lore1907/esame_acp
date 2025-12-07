import stomp 
import random, time

NUM_RICHIESTE = 10

class ClientListener(stomp.ConnectionListener): 

    def on_error(self, frame):
        print(f"[CLIENT LISTENER] Ricevuto messaggio di errore {frame.body}")

    def on_message(self, frame):
        print(f"[CLIENT LISTENER] Ricevuto messaggio {frame.body}")


def genera_richieste():

    tipi_richiesta = ["deposita", "preleva"]

    tipo_scelto = random.choice(tipi_richiesta)
    if(tipo_scelto == "deposita"):
        id_articolo = random.randint(1,100)
        return tipo_scelto, id_articolo
    else:
        return tipo_scelto, ""

if __name__ == "__main__": 

    try:
        conn = stomp.Connection(auto_content_length=False)
        conn.set_listener('', ClientListener())
        conn.connect(wait=True)
        conn.subscribe('/queue/response', id=1, ack="auto")
        destination = "/queue/request"

        for i in range(NUM_RICHIESTE):
            tipo_scelto, id_articolo = genera_richieste()

            message = f"{tipo_scelto}-{id_articolo}" if id_articolo != "" else f"{tipo_scelto}"
            conn.send(destination=destination, body=message, headers={"reply-to":"/queue/response"})
            time.sleep(1)
        
        while (True):
            time.sleep(60)

    except Exception as e:
        print(f"Chiusura della connessione... {e}")
    finally: 
        if(conn.is_connected()): conn.disconnect()