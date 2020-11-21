package dev.shermende.kafkadlqretry.configuration;

import dev.shermende.kafkadlqretry.configuration.properties.KafkaDlqRetryProperties;
import dev.shermende.kafkadlqretry.factory.impl.MessageListenerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaDlqRetryConfiguration implements InitializingBean {

    private final MessageListenerFactory listenerFactory;
    private final KafkaDlqRetryProperties dlqRetryProperties;
    private final ConcurrentKafkaListenerContainerFactory<Object, Object> factory;

    @Override
    public void afterPropertiesSet() {
        Optional.ofNullable(dlqRetryProperties.getConsumers())
            .ifPresent(kafkaDlqRetryConsumers -> kafkaDlqRetryConsumers
                .forEach(dlqRetryConsumer -> {
                    ConcurrentMessageListenerContainer<Object, Object> container = factory.createContainer(dlqRetryConsumer.getDlqTopic());
                    Optional.ofNullable(dlqRetryConsumer.getConcurrency()).ifPresent(container::setConcurrency);
                    container.setupMessageListener(listenerFactory.getInstance(dlqRetryConsumer.isBatch()));
                    container.start();
                }));
    }
}
