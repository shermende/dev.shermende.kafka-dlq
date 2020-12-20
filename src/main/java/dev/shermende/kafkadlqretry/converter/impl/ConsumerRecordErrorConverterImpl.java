package dev.shermende.kafkadlqretry.converter.impl;

import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordErrorConverterImpl implements ConsumerRecordErrorConverter {


    @Override
    public ProducerRecord<Object, Object> convert(
        DlqRetryConsumer consumer,
        ConsumerRecord<Object, Object> record
    ) {
        return new ProducerRecord<>(
            consumer.getErrorTopic(),
            null,
            System.currentTimeMillis(),
            record.key(),
            record.value(),
            record.headers()
        );
    }

}