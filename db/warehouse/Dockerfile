FROM mongo:latest

COPY stock.json /stock.json

CMD mongoimport --host mongo-warehouse --db warehouse --collection stockLine --drop --file /stock.json --jsonArray
