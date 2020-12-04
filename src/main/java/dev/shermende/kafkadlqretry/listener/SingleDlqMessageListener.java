package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.factory.ConsumerRecordSingleHandlerFactory;
import dev.shermende.kafkadlqretry.handler.single.impl.ConsumerRecordSingleErrorHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
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
    private final ConsumerRecordSingleErrorHandler errorService;
    private final ConsumerRecordSingleHandlerFactory serviceFactory;

    @Logging
    @Override
    public void onMessage(
        ConsumerRecord<Object, Object> record
    ) {
        try {
            log.debug("[Single record accepted] [Record:{}]", record);
            final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
            final int counter = AppUtil.extractCounter(consumer, record);
            serviceFactory.getInstance(counter >= consumer.getDelays().size()).handle(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorService.handle(record);
        }
    }

}