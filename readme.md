Reactive microservice-based order management system

The project represents a collection of reactive backend services for placing an order through online webstore.

When create order request is sent to Order Service, a number of microservices are ready to kick start upon receiving the corresponding events.
In the preceding diagram, there are six microservices shown:
- Order Service creates and updates orders.
- Warehouse Service reserves/releases stock for orders and creates shipment records.
- Finance Service processes payments and creates invoices.
- Catalogue Service contains product-related information (i.e. products, details, prices)
- Notification Service sends notifications to customers informing them about different order stages
- Customer Service contains customer-related information (i.e. name, contact infromation)

Each service is responsible for only one function and functions based on receiving and generatings events. 
Each service is independent and is not aware of its neighborhoods.

Technologies
Spring Webflux
Spring Kafka
Spring Reactive Mongo
Kafka
Zookeeper
Docker Compose to link the containers.
