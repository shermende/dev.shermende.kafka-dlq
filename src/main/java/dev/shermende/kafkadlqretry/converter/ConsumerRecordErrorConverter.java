package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Prepare kafka-message for send to error-topic
 */
@Slf4j
@Component
public class ConsumerRecordErrorConverter implements Converter<ConsumerRecordContext, ProducerRecord<Object, Object>> {

    @Override
    public ProducerRecord<Object, Object> convert(
        ConsumerRecordContext recordContext
    ) {
        return new ProducerRecord<>(
            recordContext.getDlqRetryConsumer().getErrorTopic(),
            null,
            System.currentTimeMillis(),
            recordContext.getRecord().key(),
            recordContext.getRecord().value(),
            recordContext.getRecord().headers()
        );
    }

}