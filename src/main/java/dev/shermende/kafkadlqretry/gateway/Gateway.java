package dev.shermende.kafkadlqretry.gateway;

public interface Gateway<O, I> {
    O send(I i);
}