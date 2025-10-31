from abc import ABC, abstractmethod

class IPrinterServer(ABC): 

    @abstractmethod
    def printReq(self, pathFile, typ): 
        raise NotImplementedError

class IPrinter(ABC): 

    @abstractmethod
    def print(self, pathFile, typ): 
        raise NotImplementedError