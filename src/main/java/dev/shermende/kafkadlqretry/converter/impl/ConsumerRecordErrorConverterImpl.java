package dev.shermende.kafkadlqretry.converter.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordErrorConverterImpl implements ConsumerRecordErrorConverter {

    @Logging
    @Override
    public ProducerRecord<Object, Object> convert(
        KafkaDlqRetryConsumer consumer,
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