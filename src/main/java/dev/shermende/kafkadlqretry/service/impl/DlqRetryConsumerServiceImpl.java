package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.configuration.properties.KafkaDlqRetryProperties;
import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import dev.shermende.kafkadlqretry.service.DlqRetryConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DlqRetryConsumerServiceImpl implements DlqRetryConsumerService {

    private final KafkaDlqRetryProperties kafkaDlqRetryProperties;

    @Override
    @Cacheable(value = "dlq-retry-consumer", key = "#topic")
    public Optional<DlqRetryConsumer> findOneByTopic(
        String topic
    ) {
        log.debug("[DlqRetryConsumer findOneByTopic] [{}]", topic);
        return kafkaDlqRetryProperties.getConsumers().stream()
            .filter(consumer -> consumer.getDlqTopic().equals(topic)).findFirst();
    }

}
