# Client Module

##Run
-first run example2 module
-run client module.

##commands
###get Items
- curl http://localhost:8081/items
- curl http://localhost:8081/items/v2

###get item
- curl http://localhost:8081/items/5cf0e1e90ffd344370271e08
- curl http://localhost:8081/items/v2/5cf0e1e90ffd344370271e08

### post item
- curl -d '{"id":null,"description":"Google Nest","price":199.99}' -H "Content-Type: application/json" -X POST http://localhost:8081/items


### update item
- curl -d '{"id":null,"description":"Beats HeadPhones","price":129.99}' -H "Content-Type: application/json" -X PUT http://localhost:8081/items/5cf0e1e90ffd344370271e08
