package dev.shermende.kafkadlqretry.service.impl;

import dev.shermende.kafkadlqretry.aop.annotation.Logging;
import dev.shermende.kafkadlqretry.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationService implements NotificationService {

    private final KafkaTemplate<Object, Object> template;

    @Logging
    @Override
    public void send(
        ProducerRecord<Object, Object> record
    ) {
        template.send(record);
    }

}
