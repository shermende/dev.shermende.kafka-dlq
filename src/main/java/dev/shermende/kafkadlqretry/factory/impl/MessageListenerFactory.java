package dev.shermende.kafkadlqretry.factory.impl;

import dev.shermende.kafkadlqretry.factory.AbstractFactory;
import dev.shermende.kafkadlqretry.listener.BatchDlqMessageListener;
import dev.shermende.kafkadlqretry.listener.SingleDlqMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListenerFactory extends AbstractFactory<Boolean, Object> {

    public MessageListenerFactory(
        BeanFactory beanFactory
    ) {
        super(beanFactory);
    }

    @Override
    protected void registration() {
        this.registry(true, BatchDlqMessageListener.class);
        this.registry(false, SingleDlqMessageListener.class);
    }

}