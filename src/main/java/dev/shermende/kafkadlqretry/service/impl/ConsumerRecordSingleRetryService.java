package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Profiling;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordRetryConverter;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.ConsumerRecordSingleService;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.NotificationService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordSingleRetryService implements ConsumerRecordSingleService {

    private final NotificationService notificationService;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordRetryConverter retryConverter;

    @Profiling
    @Override
    public void process(
        ConsumerRecord<Object, Object> record
    ) {
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        final int counter = AppUtil.extractCounter(consumer, record);
        try {
            Thread.sleep(consumer.getDelays().get(counter));
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            notificationService.send(retryConverter.convert(counter, consumer, record));
            log.info("[Processed as retry] [Record:{}]", record);
        }
    }

}