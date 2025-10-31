from threading import Thread, current_thread


class SkeletonThread(Thread): 

    def __init__(self, name, buf_size, conn, skel): 
        super().__init__(name=name)
        self.conn = conn
        self.skel = skel 
        self.buf_size = buf_size

    def run(self): 

        data = (self.conn.recv(self.buf_size)).decode("utf-8")

        richiesta = data.split('-')[0]
        articolo = data.split('-')[1]

        result = None
        if richiesta == "deposita": 
            id_articolo = data.split('-')[2]
            result = self.skel.deposita(articolo, id_articolo)
        
        elif richiesta == "preleva": 
            result = self.skel.preleva(articolo)
        
        else: 
            print(f"[{current_thread().name}] Richiesta {richiesta} non riconosciuta")

        response = str(result)
        self.conn.send(response.encode("utf-8"))
        
        self.conn.close()