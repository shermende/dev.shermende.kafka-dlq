package dev.shermende.kafkadlqretry.service;

import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;

import java.util.Optional;

public interface DlqRetryConsumerService {

    Optional<DlqRetryConsumer> findOneByTopic(String topic);

}
