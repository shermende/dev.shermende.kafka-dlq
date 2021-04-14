package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.support.spring.factory.Factory;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DlqMessageListener.class})
class DlqMessageListenerTest {

    @Autowired
    private DlqMessageListener dlqMessageListener;
    @MockBean
    @Qualifier("processingStrategyConverter")
    private Converter<ConsumerRecordContext, Boolean> strategyConverter;
    @MockBean
    @Qualifier("consumerRecordHandlerFactory")
    private Factory<Boolean, Handler<ConsumerRecordContext>> serviceFactory;
    @MockBean
    @Qualifier("consumerRecordConverter")
    private Converter<ConsumerRecord<Object, Object>, ConsumerRecordContext> recordContextConverter;
    @Mock
    private Handler<ConsumerRecordContext> handler;
    @Captor
    private ArgumentCaptor<ConsumerRecordContext> captor;

    @Test
    void onMessageHandleAsError() {
        val record = new ConsumerRecord<Object, Object>(
            UUID.randomUUID().toString(), new Random().nextInt(),
            System.currentTimeMillis(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        val easyRandomParams = new EasyRandomParameters();
        easyRandomParams.randomize(FieldPredicates.named("record"), () -> record);
        val easyRandom = new EasyRandom(easyRandomParams);
        val ctx = easyRandom.nextObject(ConsumerRecordContext.class);
        given(recordContextConverter.convert(record)).willReturn(ctx);
        given(strategyConverter.convert(ctx)).willReturn(true);
        given(serviceFactory.getInstance(true)).willReturn(handler);

        dlqMessageListener.onMessage(record);

        then(recordContextConverter).should(times(1)).convert(record);
        then(strategyConverter).should(times(1)).convert(ctx);
        then(serviceFactory).should(times(1)).getInstance(true);
        then(handler).should(times(1)).handle(captor.capture());
        Assertions.assertEquals(ctx, captor.getValue());
    }

    @Test
    void onMessageHandleAsRetry() {
        val record = new ConsumerRecord<Object, Object>(
            UUID.randomUUID().toString(), new Random().nextInt(),
            System.currentTimeMillis(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        val easyRandomParams = new EasyRandomParameters();
        easyRandomParams.randomize(FieldPredicates.named("record"), () -> record);
        val easyRandom = new EasyRandom(easyRandomParams);
        val ctx = easyRandom.nextObject(ConsumerRecordContext.class);
        given(recordContextConverter.convert(record)).willReturn(ctx);
        given(strategyConverter.convert(ctx)).willReturn(false);
        given(serviceFactory.getInstance(false)).willReturn(handler);

        dlqMessageListener.onMessage(record);

        then(recordContextConverter).should(times(1)).convert(record);
        then(strategyConverter).should(times(1)).convert(ctx);
        then(serviceFactory).should(times(1)).getInstance(false);
        then(handler).should(times(1)).handle(captor.capture());
        Assertions.assertEquals(ctx, captor.getValue());
    }

}