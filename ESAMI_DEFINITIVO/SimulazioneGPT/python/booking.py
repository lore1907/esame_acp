from flask import Flask, request
from pymongo import MongoClient




def get_database():
    
    CONNECTION_STRING = "mongodb://127.0.0.1:27017/"

    client = MongoClient(CONNECTION_STRING)

    return client['db']


app = Flask(__name__) 


@app.post("/reservations")
def post():
    message = request.get_json() 
    
    dbName = get_database()
    reservation_collection = dbName['reservations']

    print(f"Ricevuta prenotazione: USER {message['user']}, PEOPLE {message['people']}, TIMESLOT {message['timeSlot']}")
    try:
        reservation_collection.insert_one(message)
        print("Prenotazione aggiunta al database")
        return {"result": "success"}, 200
    except Exception as e:
        print("Errore nell inserimento sul db")
        return {"result": "failed"}, 400

    

@app.get("/stats")
def get_stats():

    dbName = get_database()
    reservation_collection = dbName['reservations']

    print(f"Ricevuta get STATS")
    timeSlots = ["19:00", "20:30", "22:00"]

    stats = {}
     
    for timeSlot in timeSlots: 
        count = reservation_collection.count_documents({"timeSlot": timeSlot})
        stats[timeSlot] = count

    return stats

