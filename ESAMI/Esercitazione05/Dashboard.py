import grpc, sys
import statistics_pb2, statistics_pb2_grpc

def run(port): 

    with grpc.insecure_channel('localhost:'+str(port)) as channel:

        stub = statistics_pb2_grpc.StatManagerStub(channel)

        sensor_response = list(stub.getSensors(statistics_pb2.Empty()))
        
        for sensor in sensor_response: 
            response = stub.getMean(statistics_pb2.MeanRequest(sensor_id=sensor._id, data_type=sensor.data_type))

            print(f'[DASHBOARD] Mean: {response.value}')



if __name__ == "__main__":
    try: 
        port = sys.argv[1]
    except IndexError: 
        print("Please specify port...")
        sys.exit(-1)


    run(port)