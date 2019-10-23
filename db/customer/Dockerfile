FROM mongo:latest

COPY customers.json /customers.json

CMD mongoimport --host mongo-customer --db catalogue --collection customer --drop --file /customers.json --jsonArray
