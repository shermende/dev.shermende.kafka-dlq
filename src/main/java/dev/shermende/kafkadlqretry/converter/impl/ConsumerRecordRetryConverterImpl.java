package dev.shermende.kafkadlqretry.converter.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Profiling;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordRetryConverterImpl implements ConsumerRecordRetryConverter {

    @Profiling
    @Override
    public ProducerRecord<Object, Object> convert(
        int counter,
        DlqRetryConsumer consumer,
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