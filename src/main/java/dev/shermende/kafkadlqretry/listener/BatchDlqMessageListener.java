package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.aop.annotation.Profiling;
import dev.shermende.kafkadlqretry.factory.impl.ConsumerRecordBatchServiceFactory;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordBatchErrorService;
import dev.shermende.kafkadlqretry.util.AppUtil;
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
    private final ConsumerRecordBatchErrorService errorService;
    private final ConsumerRecordBatchServiceFactory serviceFactory;

    @Profiling
    @Override
    public void onMessage(
        List<ConsumerRecord<Object, Object>> records
    ) {
        try {
            final ConsumerRecord<Object, Object> record = records.stream().findFirst().orElseThrow();
            final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
            final int counter = AppUtil.extractCounter(consumer, record);
            serviceFactory.getInstance(counter >= consumer.getDelays().size()).process(records);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errorService.process(records);
        }
    }

}