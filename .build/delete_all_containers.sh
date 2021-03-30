#! /bin/bash

docker rm source_db -f
docker rm target_db -f
docker rm debezium -f
docker rm kafka -f
docker rm zookeeper -f