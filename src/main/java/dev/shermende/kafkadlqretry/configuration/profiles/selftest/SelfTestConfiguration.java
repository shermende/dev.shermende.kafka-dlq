package dev.shermende.kafkadlqretry.configuration.profiles.selftest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shermende.kafkadlqretry.configuration.profiles.selftest.properties.SelfTestProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Configuration
@EnableScheduling
@Profile({"self-test"})
@RequiredArgsConstructor
public class SelfTestConfiguration {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<Object, Object> template;

    @SneakyThrows
    @Scheduled(fixedDelay = 10000)
    public void scheduled() {
        template.send(new ProducerRecord<>(selfTestProperties().getTopic(),
            objectMapper.writeValueAsString(Collections.singleton(UUID.randomUUID().toString()))));
    }

    @KafkaListener(topics = "${dev.shermende.self-test.topic}")
    public void message(
        ConsumerRecord<Object, Object> record
    ) {
        try {
            throw new IllegalArgumentException("this exception is demo");
        } catch (Exception e) {
            log.error(e.getMessage());
            template.send(
                new ProducerRecord<>(
                    selfTestProperties().getDlqTopic(),
                    null,
                    System.currentTimeMillis(),
                    record.key(),
                    record.value(),
                    record.headers()
                )
            );
        }
    }

    @Bean
    @Validated
    @ConfigurationProperties("dev.shermende.self-test")
    public SelfTestProperties selfTestProperties() {
        return new SelfTestProperties();
    }

}