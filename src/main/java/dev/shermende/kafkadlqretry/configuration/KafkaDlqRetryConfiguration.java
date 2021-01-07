package dev.shermende.kafkadlqretry.configuration;

import dev.shermende.kafkadlqretry.configuration.properties.KafkaDlqRetryProperties;
import dev.shermende.kafkadlqretry.listener.DlqMessageListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ErrorHandler;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class KafkaDlqRetryConfiguration {

    private final ErrorHandler errorHandler;
    private final DlqMessageListener messageListener;
    private final KafkaDlqRetryProperties dlqRetryProperties;
    private final ConcurrentKafkaListenerContainerFactory<Object, Object> factory;
    private final Map<String, ConcurrentMessageListenerContainer<Object, Object>> consumerContainers = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        Optional.of(dlqRetryProperties.getConsumers())
            .ifPresent(kafkaDlqRetryConsumers -> kafkaDlqRetryConsumers
                .forEach(dlqRetryConsumer -> {
                    // create container
                    final ConcurrentMessageListenerContainer<Object, Object> container = factory.createContainer(dlqRetryConsumer.getDlqTopic());
                    // set message listener
                    container.setupMessageListener(messageListener);
                    // set error handler
                    container.setErrorHandler(errorHandler);
                    // set concurrency options
                    Optional.ofNullable(dlqRetryConsumer.getConcurrency()).ifPresent(container::setConcurrency);
                    // put to map
                    consumerContainers.put(dlqRetryConsumer.getDlqTopic(), container);
                    // start container
                    consumerContainers.get(dlqRetryConsumer.getDlqTopic()).start();
                }));
    }
}
