package dev.shermende.kafkadlqretry.factory;

import dev.shermende.kafkadlqretry.handler.batch.ConsumerRecordBatchHandler;
import dev.shermende.kafkadlqretry.handler.batch.impl.ConsumerRecordBatchErrorHandler;
import dev.shermende.kafkadlqretry.handler.batch.impl.ConsumerRecordBatchRetryHandler;
import dev.shermende.support.spring.factory.AbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordBatchHandlerFactory extends AbstractFactory<Boolean, ConsumerRecordBatchHandler> {

    public ConsumerRecordBatchHandlerFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(true, ConsumerRecordBatchErrorHandler.class);
        this.registry(false, ConsumerRecordBatchRetryHandler.class);
    }

}