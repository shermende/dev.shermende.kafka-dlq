package dev.shermende.kafkadlqretry.handler.batch.impl;

import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.gateway.impl.KafkaGateway;
import dev.shermende.kafkadlqretry.handler.batch.ConsumerRecordBatchHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordBatchRetryHandler implements ConsumerRecordBatchHandler {

    private final KafkaGateway gateway;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordRetryConverter retryConverter;

    @Logging
    @Override
    @Profiling
    public void handle(
        List<ConsumerRecord<Object, Object>> records
    ) {
        final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        final int counter = AppUtil.extractCounter(consumer, record);
        try {
            Thread.sleep(consumer.getDelays().get(counter));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            records.forEach(var -> gateway.send(retryConverter.convert(counter, consumer, var)));
            log.info("[Batch processed as retry] [Count:{}]", records.size());
        }
    }

}
