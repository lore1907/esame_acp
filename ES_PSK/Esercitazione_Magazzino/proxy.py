from i_magazzino import IMagazzino
import socket


class Proxy(IMagazzino): 

    def __init__ (self, ip, port): 
        self.ip = ip 
        self.port = port 
        self.buf_size = 1024

    def deposita(self, articolo, id): 

        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((self.ip, self.port))

        message_to_send = '-'.join(["deposita", articolo, str(id)])

        sock.send(message_to_send.encode("utf-8"))
        response = sock.recv(self.buf_size)

        return bool(response)

    def preleva(self, articolo): 
        
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((self.ip, self.port))

        message_to_send = '-'.join(["preleva", articolo])

        sock.send(message_to_send.encode("utf-8"))
        response = (sock.recv(self.buf_size)).decode("utf-8")

        return int(response)