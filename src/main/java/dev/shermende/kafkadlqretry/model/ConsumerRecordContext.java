package dev.shermende.kafkadlqretry.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Data
@Builder
public class ConsumerRecordContext {
    /**
     * Count of exceptions
     */
    @NonNull
    private Integer exceptionCounter;
    /**
     * DLQ-consumer settings
     */
    @NonNull
    private DlqRetryConsumer dlqRetryConsumer;
    /**
     * Kafka-message
     */
    @NonNull
    private ConsumerRecord<Object, Object> record;
}