import stomp, random, time
import logging


class MyListener(stomp.ConnectionListener):

    def on_message(self, frame): 
        
        logging.debug('Received response: "%s" ' % frame.body)

    
if __name__ == "__main__": 

    logging.basicConfig(filename="magazzino.log", level = logging.DEBUG, format = "[CLIENT] %(asctime)s - %(levelname)s -%(message)s")

    conn = stomp.Connection([("127.0.0.1", 61613)], auto_content_length=False)

    conn.set_listener("", MyListener())
    conn.connect(wait=True)
    conn.subscribe(destination="/queue/response", id=1, ack='auto')

    request_type = ["preleva", "deposita"]

    for i in range(10):

        request = random.choice(request_type)
        
        if request == "deposita": 

            _id = random.randint(1,100)
            MSG = request + "-" + str(_id) 
        
        elif request == "preleva":
            MSG = request

        conn.send("/queue/request", MSG, headers={"reply-to":"/queue/response"})

        logging.debug("Request: %s", MSG)


    while True:
        time.sleep(60)

    conn.disconnect()


    
            
    