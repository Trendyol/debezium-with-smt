# Debezium With Single Message Transforms (SMT)
In this project, we will create different Debezium connectors to use SMT features by tuning connector configurations.
## Project Components
This project basically includes the following components.
![](debezium-with-smt.png?raw=true "Flex Payment Report")
### 1. Customer Service
An application creates customers on the customers table in the source datasource.
### 2. Source DB
A PostgreSQL DB consists customers table.
### 3. Debezium Connect
A Debezium Connect consists customer connector to captures changes on the customers table and publishes them to the Kafka.
### 4. Kafka
The Debezium Connect produce all changes on source db as an event to kafka and user consumer consumes these events.
### 5. User Consumer
An application consumes customers table events and reflects changes on the users table in the target datasource.
### 6. Target DB
A PostgreSQL DB consists users table.
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for creating connectors with SMT. See deployment for notes on how to deploy the project.
### Prerequisites
```
Docker
Jdk11
```
### Installing
```
git clone https://github.com/Trendyol/debezium-with-smt.git
```
### Deployment
To run all the services and platforms apply the following steps.
1. Move into ".build" folder.
2. Change the kafka ADVERTISED_HOST_NAME environment to your local IP in the docker-compose.yml
3. Run "docker-compose up -d" command
   These instructions will enable the databases, zookeeper, kafka and debezium to run on your local machine.
   The filter SMT and any implementation of the JSR 223 API are not included in the Debezium by default.
   So we get these jar files and extract them into the  Debezium plug-in directories of our Kafka Connect environment by using following volumes.
```
- $PWD/jars/debezium-scripting-1.4.0.Alpha2.jar:/kafka/connect/debezium-connector-postgres/debezium-scripting-1.4.0.Alpha2.jar
- $PWD/jars/groovy-3.0.6.jar:/kafka/connect/debezium-connector-postgres/groovy-3.0.6.jar
- $PWD/jars/groovy-json-3.0.6.jar:/kafka/connect/debezium-connector-postgres/groovy-json-3.0.6.jar
- $PWD/jars/groovy-jsr223-3.0.6.jar:/kafka/connect/debezium-connector-postgres/groovy-jsr223-3.0.6.jar
```
Before you start to create connectors, make sure that all the platforms are up and running.
Container Name | Image
--- | ---
source_db | postgres:11.1-alpine
target_db | postgres:11.1-alpine
zookeeper | debezium/zookeeper:1.1
kafka | debezium/kafka:1.1
debezium | debezium/connect:1.3
### Create Topics
You can create the Debezium config, offset and status topics with the following commands.
```
bin/kafka-topics.sh --create --bootstrap-server 0.0.0.0:9092  --partitions 1 --replication-factor 1 --topic debezium_connect_config --config retention.ms=-1
bin/kafka-topics.sh --create --bootstrap-server 0.0.0.0:9092 --replication-factor 1 --partitions 25 --topic debezium_connect_offset --config retention.ms=-1
bin/kafka-topics.sh --create --bootstrap-server 0.0.0.0:9092 --replication-factor 1 --partitions 5 --topic debezium_connect_status --config retention.ms=-1
```
## Create Connectors
Before you start creating connectors, please note that to use your local ip instead of localhost.
You can find all postman collection in _**.build**_ folder.
### Create Base Connector
Creates a base connector that captures changes on the customers table and produces them to debezium.public.customers topic.
```
curl --location --request POST 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{ 
"name": "customer-connector",
"config": { 
   "connector.class": "io.debezium.connector.postgresql.PostgresConnector", 
   "tasks.max": "1", 
   "group.id": "debezium-with-postgres",
   "database.hostname": "localhost", 
   "database.port": "5432", 
   "database.user": "debezium", 
   "database.password": "123qwe", 
   "database.dbname" : "customer",
   "database.server.name": "debezium", 
   "database.whitelist": "public", 
   "table.include.list": "public.customers",
   "heartbeat.interval.ms": "1000",
   "slot.name": "debezium_customer",
   "database.history.kafka.bootstrap.servers": "localhost:9092",
   "key.converter": "org.apache.kafka.connect.json.JsonConverter",
   "key.converter.schemas.enable": "false",
   "value.converter": "org.apache.kafka.connect.json.JsonConverter",
   "value.converter.schemas.enable": "false",
   "plugin.name":"pgoutput"
   } 
}'
```
After creating the connector you can run customer-service application to create customers, and you can see the change messages with the following command.
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic debezium.public.customers | jq
```
### Update The Base Connector For Tombstone Messages
When a delete operation occurred, Debezium generates a Kafka record called tombstone message with a value of null.
Using the connector option tombstones.on.delete you can control whether upon record deletions a tombstone event should be emitted or not.
```
curl --location --request PUT 'http://localhost:8083/connectors/customer-connector/config' \
--header 'Content-Type: application/json' \
--data-raw '{ 
   "connector.class": "io.debezium.connector.postgresql.PostgresConnector", 
   "tasks.max": "1", 
   "group.id": "debezium-with-postgres",
   "database.hostname": "localhost", 
   "database.port": "5432", 
   "database.user": "debezium", 
   "database.password": "123qwe", 
   "database.dbname" : "customer",
   "database.server.name": "debezium", 
   "database.whitelist": "public", 
   "table.include.list": "public.customers",
   "heartbeat.interval.ms": "1000",
   "slot.name": "debezium_customer",
   "database.history.kafka.bootstrap.servers": "localhost:9092",
   "key.converter": "org.apache.kafka.connect.json.JsonConverter",
   "key.converter.schemas.enable": "false",
   "value.converter": "org.apache.kafka.connect.json.JsonConverter",
   "value.converter.schemas.enable": "false",
   "plugin.name":"pgoutput",
    "tombstones.on.delete": "false"
} 
'
```
### Update Base Connector For Column Black List
You can use column.blacklist option for the columns that you don't need.
These columns will be excluded from change events.
```
curl --location --request PUT 'http://localhost:8083/connectors/customer-connector/config' \
--header 'Content-Type: application/json' \
--data-raw '{ 
   "connector.class": "io.debezium.connector.postgresql.PostgresConnector", 
   "tasks.max": "1", 
   "group.id": "debezium-with-postgres",
   "database.hostname": "localhost", 
   "database.port": "5432", 
   "database.user": "debezium", 
   "database.password": "123qwe", 
   "database.dbname" : "customer",
   "database.server.name": "debezium", 
   "database.whitelist": "public", 
   "table.include.list": "public.customers",
   "heartbeat.interval.ms": "1000",
   "slot.name": "debezium_customer",
   "database.history.kafka.bootstrap.servers": "localhost:9092",
   "key.converter": "org.apache.kafka.connect.json.JsonConverter",
   "key.converter.schemas.enable": "false",
   "value.converter": "org.apache.kafka.connect.json.JsonConverter",
   "value.converter.schemas.enable": "false",
   "plugin.name":"pgoutput",
    "tombstones.on.delete": "false",
    "column.blacklist":"public.customers.created_date, public.customers.last_modified_date"
} 
'
```
### Create Connector For Initial Snapshot Filter
```
curl --location --request POST 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "customer-connector-inital-snapshot",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "tasks.max": "1",
        "group.id": "debezium-with-postgres",
        "database.hostname": "localhost",
        "database.port": "5432",
        "database.user": "debezium",
        "database.password": "123qwe",
        "database.dbname": "customer",
        "database.server.name": "debezium.initial-snapshot",
        "database.whitelist": "public",
        "table.include.list": "public.customers",
        "heartbeat.interval.ms": "1000",
        "slot.name": "debezium_customer_initial_snapshot",
        "database.history.kafka.bootstrap.servers": "localhost:9092",
        "key.converter": "org.apache.kafka.connect.json.JsonConverter",
        "key.converter.schemas.enable": "false",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "value.converter.schemas.enable": "false",
        "plugin.name": "pgoutput",
        "tombstones.on.delete": "false",
        "column.blacklist": "public.customers.created_date, public.customers.last_modified_date",
        "snapshot.select.statement.overrides": "public.customers",
        "snapshot.select.statement.overrides.public.customers": "select * from public.customers where birthday > '\''1980-01-01 00:00:00'\''"
    }
}'
```
Before you creating the connector you can run customer-service application to create customers, and you can see the filtered initial snapshot messages with the following command.
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic debezium.initial-snapshot.public.customers | jq
```
### Create Connector For SMT
```
curl --location --request POST 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "customer-connector-smt",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "tasks.max": "1",
        "group.id": "debezium-with-postgres",
        "database.hostname": "localhost",
        "database.port": "5432",
        "database.user": "debezium",
        "database.password": "123qwe",
        "database.dbname": "customer",
        "database.server.name": "debezium.smt",
        "database.whitelist": "public",
        "table.include.list": "public.customers",
        "heartbeat.interval.ms": "1000",
        "slot.name": "debezium_customer_smt",
        "database.history.kafka.bootstrap.servers": "localhost",
        "key.converter": "org.apache.kafka.connect.json.JsonConverter",
        "key.converter.schemas.enable": "false",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "value.converter.schemas.enable": "false",
        "plugin.name": "pgoutput",
        "tombstones.on.delete": "false",
        "column.blacklist": "public.customers.created_date, public.customers.last_modified_date",
        "transforms": "filter",
        "transforms.filter.type": "io.debezium.transforms.Filter",
        "transforms.filter.language": "jsr223.groovy",
        "transforms.filter.condition": "value.op == 'u' && value.before.active == false && value.after.active == true",
        "transforms.filter.topic.regex": "debezium.smt.public.customers"
    }
}
```
After you create the connector, connector sends event only whe active column change from false to true. You can see the filtered messages with the following command.
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic debezium.smt.public.customers | jq
```
## Authors
- Okan Yıldırım
- Betül Çetinkaya