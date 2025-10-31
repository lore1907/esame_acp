import socket, sys, time, random 
from service import Service
BUFFER_SIZE = 1024

class DispatcherProxy(Service): 

    def __init__(self, host, port, name, request_num): 
        
        self.host = host
        self.port = port 
        self.name = name
        self.request_num = request_num

    def sendCmd(self, value): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.host, int(self.port)))

        request = "sendCmd"

        message_to_send = request + "-" + str(value)

        time.sleep(random.randint(2,4))

        print(f"[CLIENT] thread : {self.name} \t\t\tsending {self.request_num} request with message: {message_to_send}\n ")
        s.send(message_to_send.encode('utf-8'))
        
        print(f"[CLIENT] thread : {self.name} \t\t\twaiting result for {self.request_num} request...\n ")


        data = s.recv(BUFFER_SIZE).decode('utf-8')
        print(f"[CLIENT] thread : {self.name} \t\t\recieved result for {self.request_num} request, result is: {data:s} \n ")
        s.close()

    def getCmd(self): 

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.host, int(self.port)))

        message_to_send = "getCmd"

        print(f"[ACTUATOR] sending #{self.request_num} request: {message_to_send}\n")

        time.sleep(1)
        s.send(message_to_send.encode('utf-8'))
        
        data = s.recv(BUFFER_SIZE).decode('utf-8')

        return data
    