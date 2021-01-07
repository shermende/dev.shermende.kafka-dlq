package dev.shermende.kafkadlqretry.gateway.impl;

import dev.shermende.kafkadlqretry.gateway.Gateway;
import dev.shermende.kafkadlqretry.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaGateway implements Gateway<Boolean, ProducerRecord<Object, Object>> {

    private final KafkaTemplate<Object, Object> template;

    @Override
    public Boolean send(
        ProducerRecord<Object, Object> producerRecord
    ) {
        log.debug("[Kafka gateway sending...] [Record:{}]", LogUtil.sanitize(producerRecord));
        template.send(producerRecord);
        return true;
    }

}