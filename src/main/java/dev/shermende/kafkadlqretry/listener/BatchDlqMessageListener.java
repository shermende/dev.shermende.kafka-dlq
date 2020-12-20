package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.factory.ConsumerRecordBatchHandlerFactory;
import dev.shermende.kafkadlqretry.handler.batch.impl.ConsumerRecordBatchErrorHandler;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchDlqMessageListener implements BatchMessageListener<Object, Object> {

    private final DlqRetryConsumerService dlqRetryConsumerService;
    private final ConsumerRecordBatchErrorHandler errorService;
    private final ConsumerRecordBatchHandlerFactory serviceFactory;

    @Logging
    @Override
    public void onMessage(
        List<ConsumerRecord<Object, Object>> records
    ) {
        try {
            log.debug("[Batch accepted] [Count:{}]", records.size());
            final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
            final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
            final int counter = AppUtil.extractCounter(consumer, record);
            serviceFactory.getInstance(counter >= consumer.getDelays().size()).handle(records);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorService.handle(records);
        }
    }

}