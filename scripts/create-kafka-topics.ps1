# Kafka Topics Creation Script for Windows PowerShell

$KAFKA_CONTAINER = "medinsight-kafka"
$BOOTSTRAP_SERVER = "localhost:9092"

Write-Host "Creating Kafka topics with production configuration..." -ForegroundColor Green

# Create audit-events topic
Write-Host "`nCreating audit-events topic..."
docker exec $KAFKA_CONTAINER kafka-topics --create `
  --bootstrap-server $BOOTSTRAP_SERVER `
  --topic audit-events `
  --partitions 3 `
  --replication-factor 1 `
  --if-not-exists `
  --config retention.ms=604800000

Write-Host "✓ Created audit-events topic (3 partitions, retention: 7 days)" -ForegroundColor Cyan

# Create notification-events topic
Write-Host "`nCreating notification-events topic..."
docker exec $KAFKA_CONTAINER kafka-topics --create `
  --bootstrap-server $BOOTSTRAP_SERVER `
  --topic notification-events `
  --partitions 3 `
  --replication-factor 1 `
  --if-not-exists `
  --config retention.ms=604800000

Write-Host "✓ Created notification-events topic (3 partitions, retention: 7 days)" -ForegroundColor Cyan

# Create DLQ topics
Write-Host "`nCreating audit-events-dlq topic..."
docker exec $KAFKA_CONTAINER kafka-topics --create `
  --bootstrap-server $BOOTSTRAP_SERVER `
  --topic audit-events-dlq `
  --partitions 1 `
  --replication-factor 1 `
  --if-not-exists `
  --config retention.ms=2592000000

Write-Host "✓ Created audit-events-dlq topic (1 partition, retention: 30 days)" -ForegroundColor Cyan

Write-Host "`nCreating notification-events-dlq topic..."
docker exec $KAFKA_CONTAINER kafka-topics --create `
  --bootstrap-server $BOOTSTRAP_SERVER `
  --topic notification-events-dlq `
  --partitions 1 `
  --replication-factor 1 `
  --if-not-exists `
  --config retention.ms=2592000000

Write-Host "✓ Created notification-events-dlq topic (1 partition, retention: 30 days)" -ForegroundColor Cyan

# List all topics
Write-Host "`nAll Kafka topics:" -ForegroundColor Yellow
docker exec $KAFKA_CONTAINER kafka-topics --list --bootstrap-server $BOOTSTRAP_SERVER

# Describe topics
Write-Host "`nTopic details:" -ForegroundColor Yellow
docker exec $KAFKA_CONTAINER kafka-topics --describe --bootstrap-server $BOOTSTRAP_SERVER
