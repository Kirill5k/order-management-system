FROM mongo:latest

COPY products.json /products.json

CMD mongoimport --host mongo-catalogue --db catalogue --collection productItem --drop --file /products.json --jsonArray
