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
     *
     */
    @NotEmpty
    private String topic;
    /**
     *
     */
    @NotEmpty
    private String dlqTopic;
    /**
     *
     */
    @NotEmpty
    private String errorTopic;
    /**
     *
     */
    @NotEmpty
    @Builder.Default
    private List<Integer> delays = Arrays.asList(60000, 120000, 180000);
    /**
     *
     */

    @NotEmpty
    @Builder.Default
    private String retryCounterHeader = "x-kafka-dlq-retry-count";
    /**
     *
     */
    private Integer concurrency;
}