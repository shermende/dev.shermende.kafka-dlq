package dev.shermende.kafkadlqretry.handler.single.impl;

import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.gateway.impl.KafkaGateway;
import dev.shermende.kafkadlqretry.handler.single.ConsumerRecordSingleHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordSingleErrorHandler implements ConsumerRecordSingleHandler {

    private final KafkaGateway gateway;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordErrorConverter errorConverter;

    @Logging
    @Override
    public void handle(
        ConsumerRecord<Object, Object> record
    ) {
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        gateway.send(errorConverter.convert(consumer, record));
        log.info("[Single record processed as error] [Record:{}]", record);
    }

}