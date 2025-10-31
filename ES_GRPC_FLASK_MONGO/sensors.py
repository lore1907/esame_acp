import requests, random 
import logging
import threading as mt 

supported_types = ["temp", "press"]
server_address = "http://127.0.0.1:5001"
NUM_REQS = 5
NUM_SENSORS = 5
DATA_MIN = 1
DATA_MAX = 50


logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def thd_func(sensor_id): 
    
    data_type = random.choice(supported_types)

    sensor_spec = {
        '_id' : sensor_id,
        'data_type' : data_type
    }

    resource_location = server_address + "/sensors"

    response = requests.post(resource_location, json = sensor_spec)

    try: 

        response.raise_for_status()
    
    except requests.exceptions.HTTPError:

        logging.error(f"[SENSOR-{sensor_id}] Error: Recieved {response.status_code} - {response.text}")
    
    else:

        logging.info(f"SENSOR-{sensor_id}] Added sensor with: {sensor_spec}")



    for i in range(NUM_REQS): 

        data = {
            'sensor_id' : sensor_id,
            'data' : random.randint(DATA_MIN, DATA_MAX)
        }

        resource_location = server_address + "/data/" + data_type
        response =  requests.post(resource_location, json=data)

        try: 
            response.raise_for_status()
        
        except requests.exceptions.HTTPError: 

            logging.error(f"[SENSOR-{sensor_id}] Error: Recieved {response.status_code} - {response.text}")
        
        else:

            logging.info(f"SENSOR-{sensor_id}] Added {data_type} data: {data}")





if __name__ == "__main__": 

    threads = []

    for i in range(1, NUM_SENSORS + 1):

        thd = mt.Thread(target=thd_func, args=(i,))
        thd.start()
        threads.append(thd)

    for thread in threads: 
        thread.join()

    logging.info("All sensors finished.")