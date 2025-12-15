#!/bin/bash

# Script to create Kafka topics with proper partitions and replication
# Run this after Kafka is started

KAFKA_CONTAINER="medinsight-kafka"
BOOTSTRAP_SERVER="localhost:9092"

echo "Creating Kafka topics with production configuration..."

# Create audit-events topic
docker exec $KAFKA_CONTAINER kafka-topics --create \
  --bootstrap-server $BOOTSTRAP_SERVER \
  --topic audit-events \
  --partitions 3 \
  --replication-factor 1 \
  --if-not-exists \
  --config retention.ms=604800000

echo "✓ Created audit-events topic (3 partitions, retention: 7 days)"

# Create notification-events topic
docker exec $KAFKA_CONTAINER kafka-topics --create \
  --bootstrap-server $BOOTSTRAP_SERVER \
  --topic notification-events \
  --partitions 3 \
  --replication-factor 1 \
  --if-not-exists \
  --config retention.ms=604800000

echo "✓ Created notification-events topic (3 partitions, retention: 7 days)"

# Create DLQ topics
docker exec $KAFKA_CONTAINER kafka-topics --create \
  --bootstrap-server $BOOTSTRAP_SERVER \
  --topic audit-events-dlq \
  --partitions 1 \
  --replication-factor 1 \
  --if-not-exists \
  --config retention.ms=2592000000

echo "✓ Created audit-events-dlq topic (1 partition, retention: 30 days)"

docker exec $KAFKA_CONTAINER kafka-topics --create \
  --bootstrap-server $BOOTSTRAP_SERVER \
  --topic notification-events-dlq \
  --partitions 1 \
  --replication-factor 1 \
  --if-not-exists \
  --config retention.ms=2592000000

echo "✓ Created notification-events-dlq topic (1 partition, retention: 30 days)"

# List all topics
echo ""
echo "All Kafka topics:"
docker exec $KAFKA_CONTAINER kafka-topics --list --bootstrap-server $BOOTSTRAP_SERVER

# Describe topics
echo ""
echo "Topic details:"
docker exec $KAFKA_CONTAINER kafka-topics --describe --bootstrap-server $BOOTSTRAP_SERVER
