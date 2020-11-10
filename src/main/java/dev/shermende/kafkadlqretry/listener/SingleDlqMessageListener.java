package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.factory.impl.ConsumerRecordSingleServiceFactory;
import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordSingleErrorService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SingleDlqMessageListener implements MessageListener<Object, Object> {

    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordSingleErrorService errorService;
    private final ConsumerRecordSingleServiceFactory serviceFactory;

    @Logging
    @Override
    public void onMessage(
        ConsumerRecord<Object, Object> record
    ) {
        try {
            final KafkaDlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
            final int counter = AppUtil.extractCounter(consumer, record);
            serviceFactory.getInstance(counter >= consumer.getDelays().size()).process(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorService.process(record);
        }
    }

}