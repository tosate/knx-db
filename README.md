# KNX database

Simplified KNX database to store which device has which group address assigned.
The database is accessed through a RESTful API provided by a Spring Boot Maven application.
swagger file: http://localhost:8080/v2/api-docs

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
