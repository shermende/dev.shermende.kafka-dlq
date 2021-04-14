package dev.shermende.kafkadlqretry.handler.impl;

import dev.shermende.kafkadlqretry.gateway.Gateway;
import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.kafkadlqretry.util.LogUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordErrorHandler implements Handler<ConsumerRecordContext> {

    @Qualifier("consumerRecordErrorConverter")
    private final Converter<ConsumerRecordContext, ProducerRecord<Object, Object>> errorConverter;
    @Qualifier("kafkaGateway")
    private final Gateway<Boolean, ProducerRecord<Object, Object>> gateway;

    @Logging
    @Override
    public void handle(
        ConsumerRecordContext recordContext
    ) {
        gateway.send(errorConverter.convert(recordContext));
        log.debug("[Record processed as error] [Record:{}]", LogUtil.sanitize(recordContext.getRecord().value()));
    }

}