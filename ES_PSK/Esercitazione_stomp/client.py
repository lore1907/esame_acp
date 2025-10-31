import stomp 
import random, time


N =  10 

class MyListener(stomp.ConnectionListener): 

    def __init__(self, conn):
        self.conn = conn
        self.count = 0
        self.total_messages = N

    def on_message(self, response): 
        print('Risposta ricevuta: "%s"' %response.body)
        self.count +=1
        if self.count == self.total_messages:
            print("Tutte le risposte ricevute. Disconnessionen in corso...")
            self.conn.disconnect()

if __name__ == "__main__": 

    conn = stomp.Connection([('127.0.0.1', 61613)])
    
    conn.connect(wait=True)
    conn.set_listener('', MyListener(conn))
    conn.subscribe(destination='/queue/risposte', id=1, ack='auto')

    for i in range(N):
        tipo_richiesta = random.choice(["deposita", "preleva"])
        
        if tipo_richiesta == "deposita": 
            id_articolo = random.randint(1,100)
            message_to_send = tipo_richiesta + "-" + str(id_articolo)        
        elif tipo_richiesta == "preleva": 
            message_to_send = tipo_richiesta
            
        conn.send('/queue/richieste', message_to_send)
        print(f"Inviato messaggio {i+1}/{N}: {message_to_send}")
        time.sleep(0.1)

    print("In attesa delle risposte...")
    while conn.is_connected():
        time.sleep(1)

    print("Client terminato.")