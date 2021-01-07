package dev.shermende.kafkadlqretry.handler.impl;

import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordExceptionHandler implements ErrorHandler {

    @Qualifier("consumerRecordErrorHandler")
    private final Handler<ConsumerRecordContext> handler;
    private final Converter<ConsumerRecord<Object, Object>, ConsumerRecordContext> recordContextConverter;

    @Override
    public void handle(
        Exception e,
        ConsumerRecord<?, ?> record
    ) {
        log.error(e.getMessage(), e);
        handler.handle(recordContextConverter.convert(
            new ConsumerRecord<>(record.topic(), record.partition(), record.timestamp(), record.key(), record.value())));
    }

}