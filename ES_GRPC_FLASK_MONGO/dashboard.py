import grpc 
import sys 
import service_pb2, service_pb2_grpc
import logging

def run(port):

    channel = grpc.insecure_channel('localhost:'+str(port))
    
    stub = service_pb2_grpc.ServiceStub(channel)

    logging.info('[DASHBOARD] Sending request for available sensors')
    
    sensors_response = stub.getSensors(service_pb2.Empty())

    sensors = []

    logging.info('[DASHBOARD] Available sensors: ')
    for sensor in sensors_response: 
        print(f'[DASHBOARD] sensor_id: {sensor._id} - data type: {sensor.data_type}')
        sensors.append(sensor)

    for sensor in sensors: 
        logging.info(f'[DASHBOARD] Sending mean request: sensor_id: {sensor._id} - data type: {sensor.data_type}')
        response = stub.getMean(service_pb2.MeanRequest(sensor_id=sensor._id, data_type=sensor.data_type))

        print(f'[DASHBOARD] Mean: {response.value}')



if __name__ == "__main__": 

    logging.basicConfig(level=logging.INFO, format=' %(asctime)s - %(levelname)s - %(message)s ')
    try: 
        port = sys.argv[1]
    
    except IndexError: 
        logging.error("Please specify arg port...")
        sys.exit(-1)

    run(port)
