from printer_impl import PrinterImpl
import multiprocess as mp


if __name__ == "__main__": 

    HOST = "localhost"
    PORT = 8001
    QUEUE = mp.Queue()

    print(f"[PRINTER SERVER] In avvio....")
    skel_impl = PrinterImpl(HOST, PORT, QUEUE)
    skel_impl.run_skel()
    