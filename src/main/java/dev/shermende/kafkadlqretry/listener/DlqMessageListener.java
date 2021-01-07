package dev.shermende.kafkadlqretry.listener;

import dev.shermende.kafkadlqretry.handler.Handler;
import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import dev.shermende.kafkadlqretry.util.LogUtil;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.factory.Factory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlqMessageListener implements MessageListener<Object, Object> {

    private final Converter<ConsumerRecordContext, Boolean> strategyConverter;
    private final Factory<Boolean, Handler<ConsumerRecordContext>> serviceFactory;
    private final Converter<ConsumerRecord<Object, Object>, ConsumerRecordContext> recordContextConverter;

    @Logging
    @Override
    public void onMessage(
        ConsumerRecord<Object, Object> record
    ) {
        log.debug("[Record accepted] [Record:{}]", LogUtil.sanitize(record.value()));
        final ConsumerRecordContext ctx = recordContextConverter.convert(record);
        serviceFactory.getInstance(strategyConverter.convert(Objects.requireNonNull(ctx))).handle(ctx);
    }

}