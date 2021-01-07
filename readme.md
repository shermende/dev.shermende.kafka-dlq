# Kafka DLQ retry
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=shermende_dev.shermende.kafka-dlq-retry&metric=alert_status)](https://sonarcloud.io/dashboard?id=shermende_dev.shermende.kafka-dlq-retry)
![Maven pipeline](https://github.com/shermende/dev.shermende.kafka-dlq-retry/workflows/Maven%20pipeline/badge.svg)

###### Workflow cycle 
![kafka-dlq-retry-cycle](https://github.com/shermende/dev.shermende.kafka-dlq-retry/blob/main/.readme/kafka-dlq-retry-cycle.png?raw=true)

## Reference
* [Kafka DLQ explain](https://www.confluent.io/blog/kafka-connect-deep-dive-error-handling-dead-letter-queues/)
* [Timeout between polls](https://www.javierholguera.com/2018/01/01/timeouts-in-kafka-clients-and-kafka-streams/#:~:text=max.poll.interval.ms,-Introduced%20with%20Kafka&text=The%20maximum%20delay%20between%20invocations,when%20using%20consumer%20group%20management.&text=If%20poll()%20is%20not,the%20partitions%20to%20another%20member.)
* [Partitioning and multithreading](https://stackoverflow.com/questions/25896109/in-apache-kafka-why-cant-there-be-more-consumer-instances-than-partitions)

## Run

### Run with docker-compose

##### Requirement
* docker 19.03.8
* docker-compose 1.25.0

##### Build and run

```
# build and run
$ docker-compose -f .dev/docker-compose-fast-start.yml -p kafka-dlq-retry-fast-start up --build -d
# show log
$ docker-compose -f .dev/docker-compose-fast-start.yml -p kafka-dlq-retry-fast-start logs -f kafka-dlq-retry
```

### Run with java

##### Requirement
* java 11

##### Create application.properties in project directory

```
# spring properties for connect to kafka
spring.kafka.producer.bootstrap-servers=kafka-host:port
spring.kafka.consumer.bootstrap-servers=kafka-host:port
# properties for define topics
dev.shermende.kafka-dlq-retry.consumers[0].topic=application.topic
dev.shermende.kafka-dlq-retry.consumers[0].dlq-topic=application.topic.dlq
dev.shermende.kafka-dlq-retry.consumers[0].error-topic=application.topic.error
# property for delay
dev.shermende.kafka-dlq-retry.consumers[0].delays=200,300,400
# property for concurreny and multithreading
dev.shermende.kafka-dlq-retry.consumers[0].concurrency=5
```

##### Build and run

```
# build 
$ ./mvnw clean package
# run
$ java -jar target/application.jar
```

## Troubleshooting

##### Problem
``` 
org.apache.kafka.clients.consumer.CommitFailedException: Commit cannot be completed since the group has already rebalanced and assigned the partitions to another member. This means that the time between subsequent calls to poll() was longer than the configured max.poll.interval.ms, which typically implies that the poll loop is spending too much time message processing. You can address this either by increasing max.poll.interval.ms or by reducing the maximum size of batches returned in poll() with max.poll.records.
```

##### Cure
``` 
increase 'max.poll.interval.ms' in application.properties
spring.kafka.consumer.properties.max.poll.interval.ms=300000{+ ms}
OR
reduce 'max.poll.records' in application.properties
spring.kafka.consumer.properties.max.poll.records=500{- reduced row count}
```