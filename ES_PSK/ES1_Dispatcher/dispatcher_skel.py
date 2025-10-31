from service import Service
import socket, time , sys, random 
import multiprocess as mp 



def run_func(conn, skel): 

    message = conn.recv(1204)

    request = (message.decode('utf-8')).split('-')[0]

    if request == "sendCmd" : 
        value_to_send = (message.decode('utf-8')).split('-')[1]
        skel.sendCmd(value_to_send)
        result = "ACK"
    else: 
        result = skel.getCmd()

    conn.send(result.encode('utf-8'))

    conn.close()




class DispatcherSkeleton(Service):

    def __init__(self, host, port, service): 
        self.host = host
        self.port = port
        self.service = service

    def sendCmd(self, value):
        self.service.sendCmd(value)

    def getCmd(self):
        return self.service.getCmd()

    def run_skel(self):

        host = "localhost"

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((self.host, self.port))
        print(f"Socket binded to host: " + self.host + "and port: " + str(self.port))
        self.port = s.getsockname()[1]

        s.listen(30)

        print("Socket is listening...")


        while True: 

            c, addr = s.accept()

            print("Connected to ", addr[0], ":", addr[1])

            p = mp.Process(target = run_func, args = (c, self))
            p.start()

            print("[Dispatcher Skeleton] process terminated")

        s.close()
        print("[Dispatcher SKeleton] socket is closed...")
