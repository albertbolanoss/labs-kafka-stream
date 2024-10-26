# Labs Kafka Streams

## Pre requisites

- Java 21
- Kafka
- Docker

## Setup

1. Start Apache Kafka

```bash
docker network create kafka-network

docker run -d \
  --name broker \
  --network kafka-network \
  -p 9092:9092 \
  -e KAFKA_LISTENERS=PLAINTEXT://broker:9092 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://broker:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=3 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  apache/kafka:latest
```

## Configure kafka broker

### Create topics
```bash
# Use winpty if you are using windows (winpty docker exec -it broker sh)

# 
docker exec -it -u 0 broker sh -c "cd /opt/kafka/bin && sh"
docker exec -it -u 0 broker sh -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic greetings"
docker exec -it -u 0 broker sh -c "/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic uppercase"
```

### Produce messages

```bash
docker exec -it broker sh -c "/opt/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic greetings"
```

### Consume messages
```bash
docker exec -it broker sh -c "/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic uppercase --from-beginning"
```

## If use Rancher desktop.

```bash
IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' broker)
docker exec broker sed -i "s|^advertised.listeners=.*|advertised.listeners=PLAINTEXT://$IP:9092|" /opt/kafka/config/server.properties
docker restart broker
```