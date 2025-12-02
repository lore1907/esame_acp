import requests, random 

import threading as mt 

NUM_REQ = 5
NUM_SENS = 5
supported_types = ["temp", "press"]
server_address = "http://127.0.0.1:5001" 

def thd_func(sensor_id):
    
    data_type = random.choice(supported_types)

    sensor_spec = {
        '_id' : sensor_id, 
        'data_type' : data_type
    }

    resource_location = server_address + "/sensor"
    response = requests.post(resource_location, json = sensor_spec)

    try: 
        response.raise_for_status()
    except requests.exceptions.HTTPError: 
        print(f"[SENSOR]- {sensor_id} Error: Recieved {response.status_code} - {response.text}")
    else: 
        print(f"[SENSOR] - {sensor_id} added with: {sensor_spec}")


    for i in range(NUM_REQ): 
        data = { 
            'sensor_id' : sensor_id, 
            'data': random.randint(1, 50)
        }

        resource_location = server_address + "/data/" + data_type
        response = requests.post(resource_location, json = data)

        try: 
            response.raise_for_status()
        except requests.exceptions.HTTPError: 
            print(f"[SENSOR]- {sensor_id} Error: Recieved {response.status_code} - {response.text}")
        else: 
            print(f"[SENSOR]- {sensor_id} Added {data_type} data: {data}")

if __name__ == "__main__" : 

    threads = []

    for i in range(1, NUM_SENS+1): 

        thd = mt.Thread(target=thd_func, args=(i,))
        thd.start()
        threads.append(thd)

    for thread in threads: 
        thread.join()

    print("Sensor finsihed")
