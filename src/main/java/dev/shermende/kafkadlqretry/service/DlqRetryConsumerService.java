package dev.shermende.kafkadlqretry.service;

import dev.shermende.kafkadlqretry.model.KafkaDlqRetryConsumer;

import java.util.Optional;

public interface DlqRetryConsumerService {

    Optional<KafkaDlqRetryConsumer> findOneByTopic(String topic);

}
