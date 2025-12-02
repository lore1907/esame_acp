import statistics_pb2, statistics_pb2_grpc
from pymongo import MongoClient 
from concurrent import futures
import grpc



def getDatabase(): 
    CONNECTION_STRING = "mongodb://127.0.0.1:27017"
    client = MongoClient(CONNECTION_STRING)

    return client['test']

class StatisticsServicer(statistics_pb2_grpc.StatManagerServicer): 
    
    def __init__(self, sensors, temp_data, press_temp): 
        self.sensors = sensors 
        self.temp_data = temp_data
        self.press_temp = press_temp

    def getSensors(self, request, context):
        
        sensors = self.sensors.find({})

        for sensor in sensors: 
            yield statistics_pb2.Sensor(_id=sensor['_id'], data_type=sensor['data_type'])

    def getMean(self, request, context): 
        id_s = request.sensor_id  
        data = request.data_type 
        check = str(data).lower()

        measures = []

        if check== "temp":
            measures = list(self.temp_data.find({"sensor_id" : id_s}))
        elif check == "press": 
            measures = list(self.press_temp.find({"sensor_id" : id_s}))

        elem = 0
        summ = 0

        for measure in measures: 
            summ += measure['data']
            elem += 1

        if elem == 0:
            mean = 0.0 
        else: 
            mean = summ/elem 

        return statistics_pb2.StringMessage(value=f"Sensor {id_s} - Mean {mean}")


if __name__ == "__main__": 

    dbName = getDatabase()
    sensors_coll = ddbName["sensors"]
    temp_coll = dbName["temp_data"]
    press_coll = dbName["press_data"]

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10), options=(('grpc.so_reuseport', 0),))
    statistics_pb2_grpc.add_StatManagerServicer_to_server(StatisticsServicer(sensors_coll, temp_coll, press_coll), server)

    port = 0
    port = server.add_insecure_port("[::]:" + str(port))
    print("Starting server. Listening on port " + str(port))
    server.start()
    server.wait_for_termination()