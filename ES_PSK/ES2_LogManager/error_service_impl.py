from error_service import ErrorService
import time


class ErrorServiceImpl(ErrorService): 

    def __init__(self, host, port):
        
        self.host = host
        self.port = port

    
    def writeError(self, message): 

        with open("errors.log", mode = "a") as errorsLog:

            errorsLog.write(f"[{time.ctime()}] {message} \n")
