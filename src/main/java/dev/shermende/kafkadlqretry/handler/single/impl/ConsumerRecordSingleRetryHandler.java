package dev.shermende.kafkadlqretry.handler.single.impl;

import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.gateway.impl.KafkaGateway;
import dev.shermende.kafkadlqretry.handler.single.ConsumerRecordSingleHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordSingleRetryHandler implements ConsumerRecordSingleHandler {

    private final KafkaGateway gateway;
    private final ConsumerRecordRetryConverter retryConverter;
    private final DlqRetryConsumerService dlqRetryConsumerService;

    @Logging
    @Override
    @Profiling
    public void handle(
        ConsumerRecord<Object, Object> record
    ) {
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        final int counter = AppUtil.extractCounter(consumer, record);
        try {
            Thread.sleep(consumer.getDelays().get(counter));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            gateway.send(retryConverter.convert(counter, consumer, record));
            log.info("[Single record processed as retry] [Record:{}]", record);
        }
    }

}