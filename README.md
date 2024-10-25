# Labs Kafka Streams

## Pre requisites

- Java 21
- Kafka
- Docker

## Setup

1. Start Kafka broker

```bash
docker run -d -p 9092:9092 --name broker apache/kafka:latest
```

## Configure kafka broker

```bash
# Use winpty if you are using windows (winpty docker exec -it broker sh) 
docker exec -it broker sh
cd /opt/kafka/bin

./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic greetings
./kafka-topics.sh --bootstrap-server localhost:9092 --create --topic uppercase

./kafka-console-producer.sh --broker-list localhost:9092 --topic greetings
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic uppercase --from-beginning
```

## If use Rancher desktop.

```bash
IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' broker)
docker exec broker sed -i "s|^advertised.listeners=.*|advertised.listeners=PLAINTEXT://$IP:9092|" /opt/kafka/config/server.properties
docker restart broker
```