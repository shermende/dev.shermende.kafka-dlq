package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.converter.ConsumerRecordErrorConverter;
import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.ConsumerRecordSingleService;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerRecordSingleErrorService implements ConsumerRecordSingleService {

    private final NotificationService notificationService;
    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordErrorConverter errorConverter;

    @Logging
    @Override
    public void process(
        ConsumerRecord<Object, Object> record
    ) {
        final KafkaDlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        notificationService.send(errorConverter.convert(consumer, record));
    }

}