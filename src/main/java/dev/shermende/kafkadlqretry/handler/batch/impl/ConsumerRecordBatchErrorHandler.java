package dev.shermende.kafkadlqretry.handler.batch.impl;

import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.gateway.impl.KafkaGateway;
import dev.shermende.kafkadlqretry.handler.batch.ConsumerRecordBatchHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordBatchErrorHandler implements ConsumerRecordBatchHandler {

    private final KafkaGateway gateway;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordErrorConverter errorConverter;

    @Logging
    @Override
    public void handle(
        List<ConsumerRecord<Object, Object>> records
    ) {
        final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        records.forEach(var -> gateway.send(errorConverter.convert(consumer, var)));
        log.info("[Batch processed as error] [Count:{}]", records.size());
    }

}