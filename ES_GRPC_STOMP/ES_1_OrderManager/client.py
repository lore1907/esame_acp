import grpc, sys, time, logging, random

import orderManagement_pb2, orderManagement_pb2_grpc

import multiprocess as mp 


def run(port): 

    channel = grpc.insecure_channel('localhost:' + str(port))

    stub = orderManagement_pb2_grpc.OrderManagerStub(channel)

    orders = []
    items = ['Item - A', 'ITEM - B', 'ITEM - C', 'ITEM - D', 'ITEM - E']
    destinations = ["Rome", "Naples", "San Francisco"]
    
    for i in range (5): 
        
        num = random.randint(1, len(items))
        item = random.sample(items, k = num)
        orders.append(orderManagement_pb2.Order(
            price=random.randint(100,2000),
            items=item,
            description=f'This is a sample order {str(i)}',
            destination=random.choice(destinations)))

    for order in orders: 
        response = stub.addOrder(order)
        order = stub.getOrder(response)

    order_search_result = stub.searchOrders(orderManagement_pb2.StringMessage(value='Item - A'))

    for order in order_search_result:
        print(order)

    proc_order_iterator = generate_orders()
    shipments = stub.processOrders(proc_order_iterator)

    for shipment in shipments: 
        print("Shipment : ", shipment)

def generate_orders():

    ord1 = orderManagement_pb2.Order(
        id='103', price=2222,
        items=['Item - A', 'Item - B'],
        description = 'des1',
        destination = "San Francisco"
    )

    ord2 = orderManagement_pb2.Order(
        id='105', price=2221,
        items=['Item - A', 'Item - C'],
        description = 'des12',
        destination = "San Francisco"
    )

    ord3 = orderManagement_pb2.Order(
        id='111', price=2421,
        items=['Item - A', 'Item - B', 'Item - C'],
        description = 'des4',
        destination = "Salerno"
    )

    ord4 = orderManagement_pb2.Order(
        id='115', price=4421,
        items=['Item - B', 'Item - C'],
        description = 'des4 BLA',
        destination = "Napoli"
    )

    lista = []

    lista.append(ord1)
    lista.append(ord2)
    lista.append(ord3)
    lista.append(ord4)

    for proc_orders in lista: 
        yield proc_orders


if __name__ == "__main__": 

    try: 
        port = sys.argv[1]
    except IndexError: 
        print("Please specify port...")

    logging.basicConfig()
    run(port)
    