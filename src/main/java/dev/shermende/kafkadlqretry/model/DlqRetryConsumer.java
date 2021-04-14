package dev.shermende.kafkadlqretry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DlqRetryConsumer implements Serializable {
    /**
     * Main topic
     */
    @NotEmpty
    private String topic;
    /**
     * DLQ topic
     */
    @NotEmpty
    private String dlqTopic;
    /**
     * Topic for errors
     */
    @NotEmpty
    private String errorTopic;
    /**
     * Settings of delay
     */
    @NotEmpty
    @Builder.Default
    private List<Integer> delays = Arrays.asList(60000, 120000, 180000);
    /**
     * Header for count of exceptions
     */
    @NotEmpty
    @Builder.Default
    private String retryCounterHeader = "x-kafka-dlq-retry-count";
    /**
     * Count of concurrency
     * Attention!!! Depended from count of partitions
     */
    private Integer concurrency;
}