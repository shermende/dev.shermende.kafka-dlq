package dev.shermende.kafkadlqretry.converter.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordRetryConverterImpl implements ConsumerRecordRetryConverter {

    @Logging
    @Override
    public ProducerRecord<Object, Object> convert(
        int counter,
        KafkaDlqRetryConsumer consumer,
        ConsumerRecord<Object, Object> record
    ) {
        final ProducerRecord<Object, Object> producerRecord =
            new ProducerRecord<>(
                consumer.getTopic(),
                null,
                System.currentTimeMillis(),
                record.key(),
                record.value(),
                record.headers()
            );
        producerRecord.headers().add(consumer.getRetryCounterHeader(), String.valueOf((counter + 1)).getBytes());
        return producerRecord;
    }

}