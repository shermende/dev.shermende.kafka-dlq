package dev.shermende.kafkadlqretry.handler.impl;

import dev.shermende.kafkadlqretry.gateway.Gateway;
import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.kafkadlqretry.util.LogUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordRetryHandler implements Handler<ConsumerRecordContext> {

    @Qualifier("consumerRecordRetryConverter")
    private final Converter<ConsumerRecordContext, ProducerRecord<Object, Object>> retryConverter;
    @Qualifier("consumerRecordDelayConverter")
    private final Converter<ConsumerRecordContext, Long> consumerRecordDelayConverter;
    @Qualifier("kafkaGateway")
    private final Gateway<Boolean, ProducerRecord<Object, Object>> gateway;

    @Logging
    @Override
    @Profiling
    public void handle(
        ConsumerRecordContext recordContext
    ) {
        try {
            Thread.sleep(Objects.requireNonNull(consumerRecordDelayConverter.convert(recordContext)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            gateway.send(retryConverter.convert(recordContext));
            log.debug("[Record processed as retry] [Round:{}] [Record:{}]", recordContext.getExceptionCounter(), LogUtil.sanitize(recordContext.getRecord().value()));
        }
    }

}