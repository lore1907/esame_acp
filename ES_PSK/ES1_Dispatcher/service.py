from abc import ABC, abstractmethod

class Service(ABC): 

    @abstractmethod
    def getCmd(self):
        raise NotImplementedError

        
    @abstractmethod
    def sendCmd(self, value):
        raise NotImplementedError