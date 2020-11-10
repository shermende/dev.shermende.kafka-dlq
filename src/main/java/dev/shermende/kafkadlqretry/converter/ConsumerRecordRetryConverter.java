package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface ConsumerRecordRetryConverter {

    ProducerRecord<Object, Object> convert(
        int counter,
        KafkaDlqRetryConsumer retryConsumer,
        ConsumerRecord<Object, Object> record
    );

}
