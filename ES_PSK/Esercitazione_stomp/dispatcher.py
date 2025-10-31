import time, stomp
from proxy import Proxy

N = 10 

class MyListener(stomp.ConnectionListener): 

    def __init__(self, conn, ip , port): 
        self.conn = conn 
        self.ip = ip
        self.port = port

    def on_message(self, richiesta): 
        print('Richiesta ricevuta "%s" ' % richiesta.body)
        tipo = richiesta.body.split('-')[0]
        
        proxy = Proxy(self.ip, self.port)

        

        if tipo == "deposita": 
            id_articolo = richiesta.body.split('-')[1]
            result = proxy.deposita(id_articolo)
        elif tipo == "preleva": 
            result = proxy.preleva()
        
        message = str(result)
        print(f"Risposta inviata {message}")
        self.conn.send('/queue/risposte', message)


if __name__ == "__main__": 

    IP = "localhost"
    PORT = 8001

    conn = stomp.Connection([('127.0.0.1', 61613)])
    conn.set_listener('', MyListener(conn, IP, int(PORT)))
    conn.connect(wait=True)

    conn.subscribe(destination="/queue/richieste", id = 1, ack ='auto')
    time.sleep(60)

    conn.disconnect()