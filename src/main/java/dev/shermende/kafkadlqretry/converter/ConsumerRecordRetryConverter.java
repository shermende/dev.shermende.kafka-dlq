package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordRetryConverter implements Converter<ConsumerRecordContext, ProducerRecord<Object, Object>> {

    @Override
    public ProducerRecord<Object, Object> convert(
        ConsumerRecordContext recordContext
    ) {
        final ProducerRecord<Object, Object> producerRecord =
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
            String.valueOf((recordContext.getCounter() + 1)).getBytes()
        );
        return producerRecord;
    }

}