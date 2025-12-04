from flask import Flask, request

app = Flask(__name__)


@app.post('/update_history')
def update_history(): 

    try: 
        body = request.get_json()
        operation = body["operation"]
        serial_number = body["serial_number"]

    except KeyError: 
        print("[UPDATE HISTORY] Bad request")
        return {'result': "FAILURE"}, 400

    try:
        with open("history.txt", "a") as f:  
            f.write(f"{operation}-{serial_number} \n")
            print(f"[UPDATE_HISTORY] Scritto su file : {operation}-{serial_number}")
    except Exception as e: 
        print(f"Errore nella scrittura su file {e}")
        return {"result": "FILE_ERROR"}, 500 

    return {"result": "SUCCESS"}, 200


if __name__ == "__main__": 

    app.run(debug=True, port=5001)