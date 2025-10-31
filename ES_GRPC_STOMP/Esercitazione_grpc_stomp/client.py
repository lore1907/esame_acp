import stomp, random, time

class MyListener(stomp.ConnectionListener): 

    def on_message(self, frame): 
        print('[CLIENT] Recieved response: "%s"' %frame.body)

    
if __name__ == "__main__": 

    conn = stomp.Connection([('127.0.0.1', 61613)])

    conn.set_listener("", MyListener())

    conn.connect(wait=True)
    conn.subscribe(destination="/queue/response", id=1, ack="auto")

    products = ["smartphone", "laptop"]
    


    for i in range(15): 
        
        if i < 10 : 
            request = "deposita"
            id = random.randint(1,100)
            product = random.choice(products)
            MSG = request + "-" + str(id) + "-" + product

        else: 

            MSG = "preleva"

        conn.send('/queue/request', MSG)
        print("[CLIENT] Request: ", MSG)

    MSG = "svuota"

    conn.send("/queue/request", MSG)
    print("[CLIENT] Request: ", MSG)

    while True: 
        time.sleep(60)

    conn.disconnect()