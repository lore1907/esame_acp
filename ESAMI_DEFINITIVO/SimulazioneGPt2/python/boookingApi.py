from flask import Flask, request
from pymongo import MongoClient

CONNECTION_STRING = "mongodb://127.0.0.1:27017"

def getDB():

    client = MongoClient(CONNECTION_STRING)

    return client['BookingDatabase']



app = Flask(__name__)


@app.post("/reservations")
def add_reservation():

    body = request.get_json()
    dbName = getDB()
    coll = dbName["reservations"]

    try:
        coll.insert_one(body)
    except Exception as e: 
        print(f"Errore nell'inserimento della prenotazione {e}")
        return {"result" : "fallimento"}, 400
    else:
        print(f"Prenotazione inserita con successo: {body}")
        return {"result" : "successo"}, 200



@app.get("/stats")
def get_stats():
    dbName = getDB()
    coll = dbName["reservations"]

    timeSlots = ["19:00", "20:30", "22:00"]
    stats = {}
    counter = 0

    for timeSlot in timeSlots:
        count = coll.count_documents({"timeSlot" : timeSlot})
        stats[timeSlot] = count

    return stats