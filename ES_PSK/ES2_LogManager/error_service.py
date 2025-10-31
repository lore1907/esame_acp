from abc import ABC, abstractmethod

class ErrorService(ABC): 

    @abstractmethod
    def writeError(self, message):
        raise NotImplementedError