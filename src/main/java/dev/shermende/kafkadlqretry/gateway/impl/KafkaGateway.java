package dev.shermende.kafkadlqretry.gateway.impl;

import dev.shermende.kafkadlqretry.gateway.Gateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaGateway implements Gateway<ListenableFuture<SendResult<Object, Object>>, ProducerRecord<Object, Object>> {

    private final KafkaTemplate<Object, Object> template;

    @Override
    public ListenableFuture<SendResult<Object, Object>> send(
        ProducerRecord<Object, Object> producerRecord
    ) {
        log.debug("[Kafka gateway sending...] [Record:{}]", producerRecord);
        return template.send(producerRecord);
    }

}