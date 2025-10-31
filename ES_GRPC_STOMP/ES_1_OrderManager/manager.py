import orderManagement_pb2, orderManagement_pb2_grpc
import uuid, logging, grpc, time
from concurrent import futures
class Manager(orderManagement_pb2_grpc.OrderManagerServicer):

    def __init__(self): 
        self.order_dict = {}

    def addOrder(self, request, context):

        print("[MANAGER] crezione ID per l' ordine")

        order_id = str(uuid.uuid1())
        request.id = order_id
        self.order_dict[order_id] = request

        print(f"[MANAGER] Ordine{order_id} aggiunto con successo.")
        response = orderManagement_pb2.StringMessage(value=order_id)

        return response

    def getOrder(self, request, context):

        print(f"[MANAGER] ricevuta richiesta get per l'ordine:  {request.value}")

        key = request.value

        ''' 
        ## funzionalmente identico ma piu elegante 
        response = self.order_dict.get(key, orderManagement_pb2.Order())
        ''' 

        if key in self.order_dict.keys():
            order = self.order_dict[key]
            logging.debug("[MANAGER] getOrder: ritornando order" + str(order))
        else:
            logging.debug(f"[MANAGER] getOrder: order {str(request.value)} not found")
            order = orderManagement_pb2.Order()

        return order


    def searchOrders(self, request, context):
        
        print(f"[MANAGER] cercando gli ordini con i seguenti items: {request.value}")

        matching_orders = self.searchInventory(request.value)
        
        for order in matching_orders: 
            yield order

    def searchInventory(self, query):

        matching_orders = []

        for order_id, order in self.order_dict.items():
            for itm in order.items: 
                if query in itm:
                    matching_orders.append(order)
                    break 

        return matching_orders

    def processOrders(self, request_iterator, context):

        print(f"[MANAGER] processOrders: processando gli ordini per la spedizione...")

        location_dict = {}

        for order in request_iterator: 
            
            if order.destination not in location_dict.keys(): 
                location_dict[order.destination] = [order]
            else: 
                location_dict[order.destination].append(order)


        for key, values in location_dict.items(): 

            shipment_id = uuid.uuid1()
            shipment = orderManagement_pb2.CombinedShipment(shipment_id=str(shipment_id), orders=location_dict[key], status="PROCESSED")

            yield shipment


def serve():

    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10), options=(('grpc.so_reuseport', 0),))
    orderManagement_pb2_grpc.add_OrderManagerServicer_to_server(Manager(), server)

    port = 0
    port = server.add_insecure_port('[::]:' + str(port))

    server.start()
    print("[MANAGER] server listening on port: " + str(port))
    server.wait_for_termination()

if __name__ == "__main__": 

    logging.basicConfig(format='%(asctime)s %(message)s', level=logging.DEBUG)
    serve()
