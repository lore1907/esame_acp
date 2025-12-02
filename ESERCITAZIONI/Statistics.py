import grpc
from pymongo import MongoClient

import statistics_pb2, statistics_pb2_grpc
from concurrent import futures


def getDatabase(): 

    CONNECTION_STRING = "mongodb://127.0.0.1:27017"
    client = MongoClient(CONNECTION_STRING)

    return client['test']


class StatisticsManager(statistics_pb2_grpc.StatManagerServicer): 

    def __init__(self, sensor_collection, temp_data, press_data): 
        self.sensor_collection = sensor_collection
        self.temp_data = temp_data
        self.press_data = press_data

    def getSensors(self, request, context): 
        print("Get Sensors requested. Returning sensors infos...")

        sensors = self.sensor_collection.find({})

        sensor_count = 0 

        for sensor in sensors: 
            try:
                id_s = sensor.get('_id')
                data_t = sensor.get('data_type')
                sensor_count += 1
                print(f"Sensor {str(id_s) - {data_t}}")
            except Exception as e: 
                print("[GET SENSORS] Failed retrieving one of the request fields... skip")
                continue

            yield statistics_pb2.Sensor(_id=id_s, data_type=data_t)
        
        if sensor_count == 0 :
            print("No sensors were found.")
        else: 
            print(f"{sensor_count} sensors were found.")

    def getMean(self, request, context):
        id_sensor = request.sensor_id
        data_type = request.data_type

        measures = None

        check = data_type.lower()
        
        if check == "temp": 
            measures = self.temp_data.find({'sensor_id' : id_sensor})
        elif check == "press":
            measures = self.temp_data.find({'sensor_id' : id_sensor})
        else: 
            contextset_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details(f"Invalid data_type")
            return statistics_pb2.StringMessage()

        mean, elem = 0

        for measure in measures: 
            try:
                mean += measure
                elem += 1
            except Exception as e:
                print("[GET MEAN] Failed retrieving one of the measurement")
                continue

        if elem == 0: 
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details(f"No measurement found for sensor {id_sensor}")
            return statistics_pb2.StringMessage()

        mean = mean/elem
        return statistics_pb2.StringMessage(value=str(mean))

if __name__ == "__main__": 

    db = getDatabase()
    sensor_collection = db['sensors']
    temp_data = db['temp_data']
    press_data = db['press_data']

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10), options=(("grpc.so_reuseport", 0),))
    statistics_pb2_grpc.add_StatManagerServicer_to_server(StatisticsManager(sensor_collection, temp_data, press_data), server)