package dev.shermende.kafkadlqretry.configuration.properties;

import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaDlqRetryProperties implements Serializable {
    @Valid
    private List<KafkaDlqRetryConsumer> consumers;
}