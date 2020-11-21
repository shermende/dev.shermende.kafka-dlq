package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface ConsumerRecordErrorConverter {

    ProducerRecord<Object, Object> convert(
        DlqRetryConsumer retryConsumer,
        ConsumerRecord<Object, Object> record
    );

}