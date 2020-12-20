package dev.shermende.kafkadlqretry.configuration;

import dev.shermende.kafkadlqretry.configuration.properties.KafkaDlqRetryProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.validation.annotation.Validated;

@Slf4j
@EnableKafka
@EnableCaching
@Configuration
public class ApplicationConfiguration {

    @Bean
    @Validated
    @ConfigurationProperties("dev.shermende.kafka-dlq-retry")
    public KafkaDlqRetryProperties kafkaDlqRetryProperties() {
        return new KafkaDlqRetryProperties();
    }

}