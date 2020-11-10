package dev.shermende.kafkadlqretry.service;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface NotificationService {

    void send(
        ProducerRecord<Object, Object> record
    );

}
