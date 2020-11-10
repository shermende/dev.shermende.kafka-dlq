package dev.shermende.kafkadlqretry.factory.impl;

import dev.shermende.kafkadlqretry.factory.AbstractFactory;
import dev.shermende.kafkadlqretry.service.ConsumerRecordBatchService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordBatchErrorService;
import dev.shermende.kafkadlqretry.service.impl.ConsumerRecordBatchRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerRecordBatchServiceFactory extends AbstractFactory<Boolean, ConsumerRecordBatchService> {

    public ConsumerRecordBatchServiceFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(true, ConsumerRecordBatchErrorService.class);
        this.registry(false, ConsumerRecordBatchRetryService.class);
    }

}