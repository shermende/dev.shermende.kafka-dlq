package dev.shermende.kafkadlqretry.handler;

public interface Handler<T> {
    void handle(T t);
}