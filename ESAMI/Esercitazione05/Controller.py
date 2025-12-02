from pymongo import MongoClient
from flask import Flask, request


app = Flask(__name__)

def getDatabase(): 
    CONNECTION_STRING = "mongodb://127.0.0.1:27017"
    client = MongoClient(CONNECTION_STRING)

    return client['test']


@app.post('/data/<data_type>')
def store_data(data_type): 

    body = request.get_json()
    db = getDatabase()

    if data_type == "temp": 
        data_collection = db['temp_data']
    elif data_type == "press": 
        data_collection = db['press_data']
    else: 
        print("Data type error. Supportet data types are: temp, press")
        return {'result': 'Unsupported data type'}, 400

    try: 
        data_collection.insert_one(body)
    except Exception as e: 
        print("[STORE DATA] Operation failed")
        return {'result': 'failed - ' + str(e)}, 500
    else: 
        return {'result': 'success'}


@app.post('/sensor')
def add_sensor(): 

    body = request.get_json()
    db = getDatabase()

    sensor_collection = db['sensors']

    result = None

    try: 
        result = sensor_collection.insert_one(body)
    except Exception as e: 
        return {"result": "failed - " + str(e)}, 500
    else: 
        return {'result': 'success'}


if __name__ == "__main__": 
    app.run(debug=True)