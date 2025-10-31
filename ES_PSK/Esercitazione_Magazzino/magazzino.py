from skeleton import Skeleton
from i_magazzino_impl import IMagazzinoImpl



if __name__ == "__main__":

    IP = "localhost"
    PORT = 0
    QUEUE_SIZE = 5

    magazzino = IMagazzinoImpl(IP, PORT, QUEUE_SIZE)
    skel = Skeleton(IP, PORT, magazzino)
    skel.run_skel()
    print("[MAGAZZINO SERVER] Started")