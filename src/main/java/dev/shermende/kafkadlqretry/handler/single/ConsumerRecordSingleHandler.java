package dev.shermende.kafkadlqretry.handler.single;

import dev.shermende.kafkadlqretry.handler.Handler;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerRecordSingleHandler extends Handler<ConsumerRecord<Object, Object>> {

}