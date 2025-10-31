from services import Services 
import socket 


class Proxy(Services): 

    def __init__(self, ip, port): 
        self.ip = ip 
        self.port = port
    

    def preleva(self): 
        
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.ip, self.port))

        message_to_send = "preleva" 
        s.send(message_to_send.encode("utf-8"))

        risposta = s.recv(1024).decode("utf-8")

        return risposta


    def deposita(self, id_articolo): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.ip, self.port))

        message_to_send = "deposita" + "-" + str(id_articolo)
        s.send(message_to_send.encode("utf-8"))

        risposta = s.recv(1024).decode("utf-8")

        return risposta


