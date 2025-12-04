from pymongo import MongoClient 
from flask import Flask, request

app = Flask("__name__")

def get_collection(): 
    CONNECTION_STRING = "mongodb://127.0.0.1:27017"

    client = MongoClient(CONNECTION_STRING)

    dbName = client['db_prenotazioni'] 

    return dbName["coll_prenotazioni"]


@app.post("/database/create")
def create(): 
    message = request.get_json()

    prenotazioni = get_collection()

    try:
        prenotazioni.insert_one(message)
    except Exception as e: 
        print("[CREATE] Prenotazione fallito.")
        return {"result": "failed -" +str(e)}, 500
    else: 
        print("[CREATE] Prenotazione avvenuto con successo.")
        return {"result": "success"}, 200


@app.put("/database/update")
def update_database(): 
    message = request.get_json()

    operator = message["operator"]
    nights = message["nights"]
    discount = message["discount"]

    prenotazioni = get_collection()

    try: 
        query = {
            "operator": operator, 
            "nights": {"$gte": nights}
        }

        prenotazioni_cursor = prenotazioni.find(query)

        for prenotazione in prenotazioni_cursor: 
            costo = prenotazione["cost"]
            if (isinstance(costo, str)):
                costo = float(costo)

            nuovo_costo = costo-discount
            if nuovo_costo < 0:
                nuovo_costo = 0

            ## anche se non inserito dal document in mongodb viene assegnato un id automaticamente
            prenotazioni.update_one( {"_id": prenotazione["_id"]}, {"$set":{"cost": nuovo_costo}})

    except Exception as e: 
        print("[UPDATE] Prenotazione fallito.")
        return {"result": "failed -" +str(e)}, 500
    else: 
        print("[UPDATE] Prenotazione avvenuto con successo.")
        return {"result": "success"}, 200


if __name__ == "__main__":

    app.run(debug=True)