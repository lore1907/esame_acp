import socket, sys, time, random 
from error_service import ErrorService

BUFFER_SIZE = 1024


class Proxy(ErrorService):

    def __init__(self, host, port): 

        self.host = host
        self.port = port

    def writeError(self, message): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        try: 

            s.connect((self.host, int(self.port)))
            message_to_send = message.encode("utf-8")
           
            s.send(message_to_send)
            print(f"[PROXY] Sent message to {self.host}:{self.port} -> {message}")
            
            ack = s.recv(BUFFER_SIZE).decode("utf-8")
            print(f"[PROXY] recieved {str(ack)}")

        except ConnectionRefusedError: 

            print("[PROXY] connection refused: serrver not available")

        finally: 

            s.close()

    