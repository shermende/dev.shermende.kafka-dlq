package dev.shermende.kafkadlqretry.configuration.profiles.selftest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Configuration
@EnableScheduling
@Profile({"self-test"})
@RequiredArgsConstructor
public class SelfTestConfiguration {
    private static final String TOPIC = "self-test";

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<Object, Object> template;

    @SneakyThrows
    @Scheduled(fixedDelay = 10000)
    public void scheduled() {
        template.send(new ProducerRecord<>(TOPIC,
            objectMapper.writeValueAsString(Collections.singleton(UUID.randomUUID().toString()))));
    }

    @KafkaListener(topics = TOPIC)
    public void message(
        ConsumerRecord<Object, Object> record
    ) {
        try {
            throw new IllegalArgumentException("some unexpected exception");
        } catch (Exception e) {
            log.error(e.getMessage());
            template.send(
                new ProducerRecord<>(
                    String.format("%s.dlq", record.topic()),
                    null,
                    System.currentTimeMillis(),
                    record.key(),
                    record.value(),
                    record.headers()
                )
            );
        }
    }

}