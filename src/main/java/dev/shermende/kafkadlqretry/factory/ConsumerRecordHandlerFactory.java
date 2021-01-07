package dev.shermende.kafkadlqretry.factory;

import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.handler.impl.ConsumerRecordErrorHandler;
import dev.shermende.kafkadlqretry.handler.impl.ConsumerRecordRetryHandler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.support.spring.factory.AbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordHandlerFactory extends AbstractFactory<Boolean, Handler<ConsumerRecordContext>> {

    public ConsumerRecordHandlerFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        // handler as error
        this.registry(true, ConsumerRecordErrorHandler.class);
        // handler as retry
        this.registry(false, ConsumerRecordRetryHandler.class);
    }

}