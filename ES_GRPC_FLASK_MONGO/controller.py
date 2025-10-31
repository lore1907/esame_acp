from flask import Flask, request 
from pymongo import MongoCLient

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

app = Flask(__name__)


def get_database(): 

    CONNECTION_STIRNG = "mongodb://127.0.0.1:27017"
    client = MongoCLient(CONNECTION_STIRNG)

    return client['test']




#curl -X POST --json '{"sensor_id": 10, "data": 35}' http://127.0.0.1:5000/data/temp
#curl -X POST --json '{"sensor_id": 10, "data": 35}' http://127.0.0.1:5000/data/press
@app.post('/data/<data_type>')
def store_data(data_type): 

    body = request.get_json()
    db = get_database()


    if data_type == "temp": 
        data_collection = db['temp_data']
    elif data_type == "press": 
        data_collection = db['press_data']
    else: 
        logging.warning(f"Data type error. Supported data type: temp, press")
        return {'result' : 'Unsupported data type'}, 400


    try: 
        data_collection.insert_one(body)
    except Exception as e: 
        logging.error(f"[STORE DATA] Operation failed.")
        return {'result' : 'failed - ' + str(e)}, 500

    else: 
        logging.info(f"[STORE DATA]", data_type, "data saved on DB")
        return {'result': 'success'}

    

#curl -X POST --json '{"_id": 10, "data_tyep": "temp"}' http://127.0.0.1:5000/sensor/
#curl -X POST --json '{"_id": 20, "data_type": "press"}' http://127.0.0.1:5000/sensor/
@app.post('/sensor')
def add_sensor(): 

    body = request.get_json()
    db = get_database()

    sensors_collection = db['sensors']

    result = None 

    try: 
        result = sensors_collection.insert_one(body)
    except Exception as e: 
        logging.error("[ADD SENSOR] Operation failed")
        return {'reslult': 'failed - ' + str(e)}, 500 
    else: 
        logging.info("[ADD_SENSOR] added sensor")
        return {'result': 'success'}

if __name__ == "__main__": 

    app.run(debug=True, port=5001)

