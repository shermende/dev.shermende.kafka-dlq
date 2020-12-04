package dev.shermende.kafkadlqretry.configuration.properties;

import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaDlqRetryProperties implements Serializable {
    @Valid
    @NotEmpty(message = "DLQ consumers not defined. Examples: https://google.com")
    private List<DlqRetryConsumer> consumers = new ArrayList<>();
}