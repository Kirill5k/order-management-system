#!/bin/bash
echo "======================================     Help     ============================================"
echo "Authentication:           : mongo -u <username> -p <password> --authenticationDatabase <db name>"
echo "Show all databases:       : show dbs"
echo "Show all collections:     : show collections"
echo "Stock                     : db.stockItem.help()"
echo "All Stock                 : db.stockItem.find()"
echo "================================================================================================"
docker exec -it monogo-catalogue bash