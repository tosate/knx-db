# KNX database

Simplified KNX database to store which device has which group address assigned.
The database is accessed through a RESTful API provided by a Spring Boot Maven application.
swagger file: http://localhost:8080/v2/api-docs
swagger ui: http://localhost:8080/swagger-ui/

## Create sqlite database
```
sqlite3 knx.db
```

## Execute initialization SQL script
```
sqlite> .read src/test/resources/schema-sqlite.sql
```
## Execute insert data SQL script
```
sqlite> .read src/test/resources/data-sqlite.sql
```

## Get devices of a project as CSV file
```
curl -v -H "Accept: text/csv" "http://localhost:8080/knx-db/projects/1?format=HomeAssistant"
```
