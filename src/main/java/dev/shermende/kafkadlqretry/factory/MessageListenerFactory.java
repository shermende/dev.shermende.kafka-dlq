package dev.shermende.kafkadlqretry.factory;

import dev.shermende.kafkadlqretry.listener.BatchDlqMessageListener;
import dev.shermende.kafkadlqretry.listener.SingleDlqMessageListener;
import dev.shermende.support.spring.factory.AbstractFactory;
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