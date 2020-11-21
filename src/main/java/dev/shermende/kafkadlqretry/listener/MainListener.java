package dev.shermende.kafkadlqretry.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainListener {

    private final KafkaTemplate<Object, Object> template;

    @KafkaListener(topics = "main", groupId = "application")
    public void message(
        ConsumerRecord<Object, Object> record
    ) {
        try {
            Thread.sleep(100L);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            template.send(
                new ProducerRecord<>(
                    "dlq",
                    null,
                    null,
                    null,
                    record.value(),
                    record.headers()
                )
            );
        }

    }

}