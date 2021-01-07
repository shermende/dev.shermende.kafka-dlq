package dev.shermende.kafkadlqretry.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Data
@Builder
public class ConsumerRecordContext {
    @NonNull
    private Integer counter;
    @NonNull
    private DlqRetryConsumer dlqRetryConsumer;
    @NonNull
    private ConsumerRecord<Object, Object> record;
}