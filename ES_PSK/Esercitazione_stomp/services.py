from abc import ABC, abstractmethod


class Services(ABC): 
    
    @abstractmethod
    def preleva(self): 
        raise NotImplementedError

    @abstractmethod
    def deposita(self, id_articolo): 
        raise NotImplementedError