package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface ConsumerRecordErrorConverter {

    ProducerRecord<Object, Object> convert(
        KafkaDlqRetryConsumer retryConsumer,
        ConsumerRecord<Object, Object> record
    );

}