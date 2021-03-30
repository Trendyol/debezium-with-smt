#!/bin/bash

bin/kafka-console-consumer.sh --bootstrap-server 0.0.0.0:9092 --from-beginning --topic debezium.public.customers | jq '.'