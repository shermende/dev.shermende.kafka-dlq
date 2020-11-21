package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Profiling;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.ConsumerRecordBatchService;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.NotificationService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordBatchRetryService implements ConsumerRecordBatchService {

    private final NotificationService notificationService;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordRetryConverter retryConverter;

    @Profiling
    @Override
    public void process(
        List<ConsumerRecord<Object, Object>> records
    ) {
        final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        final int counter = AppUtil.extractCounter(consumer, record);
        try {
            Thread.sleep(consumer.getDelays().get(counter));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            records.forEach(var -> notificationService.send(retryConverter.convert(counter, consumer, var)));
            log.info("[Processed as retry in batch-mode] [Rows:{}]", records.size());
        }
    }

}
