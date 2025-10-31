import service_pb2, service_pb2_grpc 
import grpc
from pymongo import MongoClient
from concurrent import futures
import logging

def getDatabase():
    
    CONNECTION_STRING = "mongodb://127.0.0.1:27017"
    client =  MongoClient(CONNECTION_STRING)

    return client['test']

class Statistics(service_pb2_grpc.ServiceServicer): 

    def __init__(self, temp_collection, press_collection, sensors_collection):\

        self.temp_collection = temp_collection
        self.press_collection = press_collection
        self.sensors_collection = sensors_collection
        

    def getSensors(self, request, context): 
        sensor_iterator = self.sensors_collection.find()

        sensor_count = 0

        for sensor in sensor_iterator: 
            
            try:
                
                sensor_id = sensor.get('_id')
                data_type = sensor.get('data_type', 'N/A')
                sensor_count +=1
                logging.info(f"Sensor {str(sensor_id)} - {str(data_type)} ")

            except Exception as e: 
                logging.error(f"[GET SENSORS] Failed retrieving one of the request field... skipping the data. Error: {e}")
                continue

            yield service_pb2.Sensor(_id=int(sensor_id), data_type=str(data_type))

        if sensor_count == 0: 
            logging.warning("No sensors were found.")
        else: 
            logging.info(f"Sensors found: {sensor_count}")            

         
    def getMean(self, request, context): 

        id_sensor  = request.sensor_id
        data_type = request.data_type

        measures = None 
        
        if data_type == "temp": 

            measures =  self.temp_collection.find({'sensor_id': id_sensor})
        
        elif data_type == "press": 

            measures =  self.press_collection.find({'sensor_id': id_sensor})

        else: 
            
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details(f"Invalid data_type {data_type}. Must be press or temp.")
            return service_pb2.StringMessage()

        mean = 0 
        elem = 0

        for measure in measures: 
            try: 
                mean = mean + measure['data']
                elem = elem + 1
            except Exception as e: 
                logging.warning(f"[GET MEAN] Failed retrieving one of the request field... skipping the data. Error {e}")
                ##Non fallisce nel compito principale a differenza di get sensors
                continue
        
        if elem == 0: 
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details(f"No measurements found for sensor {id_sensor} - data type {data_type}")
            return service_pb2.StringMessage()


        mean = mean / elem 
        logging.info("[GET MEAN] The mean is: " + str(mean))

        return service_pb2.StringMessage(value=str(mean))





if __name__ == "__main__": 

    logging.basicConfig(level= logging.INFO, format= '%(asctime)s - %(levelname)s - %(message)s')
    
    dbName = getDatabase()
    sensors_coll = dbName["sensors"]
    temp_coll = dbName["temp_data"]
    press_coll = dbName["press_data"]

    server = grpc.server( futures.ThreadPoolExecutor(max_workers=10), options=(('grpc.so_reuseport', 0),) )
    service_pb2_grpc.add_ServiceServicer_to_server(Statistics(temp_coll, press_coll, sensors_coll), server)

    port = 0 
    port = server.add_insecure_port("[::]:" + str(port))

    logging.info('Starting server. Listening on port ' + str(port))

    server.start()

    server.wait_for_termination()