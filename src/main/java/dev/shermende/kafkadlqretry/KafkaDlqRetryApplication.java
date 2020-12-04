package dev.shermende.kafkadlqretry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaDlqRetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaDlqRetryApplication.class, args);
    }
}