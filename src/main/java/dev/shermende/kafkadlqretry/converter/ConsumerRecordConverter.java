package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordConverter implements Converter<ConsumerRecord<Object, Object>, ConsumerRecordContext> {

    @Qualifier("dlqRetryConsumerServiceImpl")
    private final DlqRetryConsumerService dlqRetryConsumerService;

    @NotNull
    @Override
    public ConsumerRecordContext convert(
        ConsumerRecord<Object, Object> record
    ) {
        val consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        val counter = AppUtil.extractCounter(consumer, record);
        return ConsumerRecordContext.builder()
            .exceptionCounter(counter)
            .dlqRetryConsumer(consumer)
            .record(record)
            .build();
    }

}