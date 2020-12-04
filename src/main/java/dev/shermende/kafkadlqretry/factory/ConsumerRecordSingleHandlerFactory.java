package dev.shermende.kafkadlqretry.factory;

import dev.shermende.kafkadlqretry.handler.single.ConsumerRecordSingleHandler;
import dev.shermende.kafkadlqretry.handler.single.impl.ConsumerRecordSingleErrorHandler;
import dev.shermende.kafkadlqretry.handler.single.impl.ConsumerRecordSingleRetryHandler;
import dev.shermende.support.spring.factory.AbstractFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordSingleHandlerFactory extends AbstractFactory<Boolean, ConsumerRecordSingleHandler> {

    public ConsumerRecordSingleHandlerFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(true, ConsumerRecordSingleErrorHandler.class);
        this.registry(false, ConsumerRecordSingleRetryHandler.class);
    }

}