from flask import Flask, request
from pymongo import MongoClient


CONNECTION_STRING = "mongodb://127.0.0.1:27017"
def getDB():

    client = MongoClient(CONNECTION_STRING)

    return client['bookingDB']


app = Flask(__name__)



@app.post("/reservations")
def post():

    body = request.get_json()
    dbName = getDB()

    try:
        reservations = dbName["reservations"]
        reservations.insert_one(body)
        print(f"Prenotazione aggiunta al db")
        return {"result":"success"}, 200
    except Exception as e:
        print("Errore, inserimento fallito")
        return {"result":"failure"}, 400

@app.get("/stats")
def get_stats():

    dbName = getDB()
    reservations = dbName["reservations"]
    timeSlots = ["19:00", "20:30", "22:00"]

    stats = {}
    count = 0

    for timeSlot in timeSlots:

        count = reservations.count_documents({"timeSlot":timeSlot})
        stats[timeSlot] = count

    return stats


    
if __name__ == "__main__":

    app.run(debug=True)