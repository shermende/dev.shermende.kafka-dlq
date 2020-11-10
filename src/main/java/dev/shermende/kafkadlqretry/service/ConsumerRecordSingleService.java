package dev.shermende.kafkadlqretry.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerRecordSingleService {

    void process(
        ConsumerRecord<Object, Object> record
    );

}
