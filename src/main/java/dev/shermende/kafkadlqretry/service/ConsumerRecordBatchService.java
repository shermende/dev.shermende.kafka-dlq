package dev.shermende.kafkadlqretry.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface ConsumerRecordBatchService {

    void process(
        List<ConsumerRecord<Object, Object>> records
    );

}
