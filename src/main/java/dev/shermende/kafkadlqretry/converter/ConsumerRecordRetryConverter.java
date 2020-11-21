package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface ConsumerRecordRetryConverter {

    ProducerRecord<Object, Object> convert(
        int counter,
        DlqRetryConsumer retryConsumer,
        ConsumerRecord<Object, Object> record
    );

}
