package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Prepare kafka-message for send to main-topic
 */
@Slf4j
@Component
public class ConsumerRecordRetryConverter implements Converter<ConsumerRecordContext, ProducerRecord<Object, Object>> {

    @Override
    public ProducerRecord<Object, Object> convert(
        ConsumerRecordContext recordContext
    ) {
        val producerRecord =
            new ProducerRecord<>(
                recordContext.getDlqRetryConsumer().getTopic(),
                null,
                System.currentTimeMillis(),
                recordContext.getRecord().key(),
                recordContext.getRecord().value(),
                recordContext.getRecord().headers()
            );
        producerRecord.headers().add(
            recordContext.getDlqRetryConsumer().getRetryCounterHeader(),
            String.valueOf((recordContext.getExceptionCounter() + 1)).getBytes()
        );
        return producerRecord;
    }

}