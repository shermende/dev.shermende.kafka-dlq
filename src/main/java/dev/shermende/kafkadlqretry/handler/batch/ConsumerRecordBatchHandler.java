package dev.shermende.kafkadlqretry.handler.batch;

import dev.shermende.kafkadlqretry.handler.Handler;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface ConsumerRecordBatchHandler extends Handler<List<ConsumerRecord<Object, Object>>> {

}