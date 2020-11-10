package dev.shermende.kafkadlqretry.factory.impl;

import dev.shermende.kafkadlqretry.factory.AbstractFactory;
import dev.shermende.kafkadlqretry.service.ConsumerRecordSingleService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordSingleErrorService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordSingleRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordSingleServiceFactory extends AbstractFactory<Boolean, ConsumerRecordSingleService> {

    public ConsumerRecordSingleServiceFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(true, ConsumerRecordSingleErrorService.class);
        this.registry(false, ConsumerRecordSingleRetryService.class);
    }

}