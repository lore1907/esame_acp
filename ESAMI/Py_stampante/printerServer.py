import sys
from printerImpl import PrinterImpl
from printer_skeleton import PrinterServerSkeleton


if __name__ == "__main__": 

    try: 
        HOST = sys.argv[1]
        PORT = sys.argv[2]
    except IndexError: 
        print("Please, specify HOST and or Port args")
        sys.exit(-1)

    printerImpl = PrinterImpl(HOST, int(PORT))
    printerImpl.run_skel()