package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.ConsumerRecordBatchService;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordBatchErrorService implements ConsumerRecordBatchService {

    private final NotificationService notificationService;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordErrorConverter errorConverter;

    @Logging
    @Override
    public void process(
        List<ConsumerRecord<Object, Object>> records
    ) {
        final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
        final KafkaDlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        records.forEach(var -> notificationService.send(errorConverter.convert(consumer, var)));
    }

}