# Kafka DLQ retry

### fast start with docker-compose

```
$ ./mvnw clean package
$ docker-compose -f .dev/docker-compose-fast-start.yml -p kafka-dlq-retry-fast-start up --build -d
$ docker-compose -f .dev/docker-compose-fast-start.yml -p kafka-dlq-retry-fast-start logs -f kafka-dlq-retry
```

### example of application.properties

```
#
spring.kafka.producer.bootstrap-servers=host:port
spring.kafka.consumer.bootstrap-servers=host:port
#
dev.shermende.kafka-dlq-retry.consumers[0].topic=application.topic
dev.shermende.kafka-dlq-retry.consumers[0].dlq-topic=application.topic.dlq
dev.shermende.kafka-dlq-retry.consumers[0].error-topic=application.topic.error
# settings for delays
dev.shermende.kafka-dlq-retry.consumers[0].delays=200,300,400
# consumers concurrency  
dev.shermende.kafka-dlq-retry.consumers[0].concurrency=5
```