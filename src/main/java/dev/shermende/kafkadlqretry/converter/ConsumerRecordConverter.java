package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import dev.shermende.kafkadlqretry.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordConverter implements Converter<ConsumerRecord<Object, Object>, ConsumerRecordContext> {

    private final DlqRetryConsumerService dlqRetryConsumerService;

    @NotNull
    @Override
    public ConsumerRecordContext convert(
        ConsumerRecord<Object, Object> record
    ) {
        final DlqRetryConsumer consumer = dlqRetryConsumerService.findOneByTopic(record.topic()).orElseThrow();
        final int counter = AppUtil.extractCounter(consumer, record);
        return ConsumerRecordContext.builder()
            .counter(counter)
            .dlqRetryConsumer(consumer)
            .record(record)
            .build();
    }

}