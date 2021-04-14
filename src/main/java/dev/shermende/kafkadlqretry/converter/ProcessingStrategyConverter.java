package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Handle strategy resolver
 * if count of exception more than size of delays then handle as error
 * if count of exception less than size of delays then handle as retry
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingStrategyConverter implements Converter<ConsumerRecordContext, Boolean> {

    @NotNull
    @Override
    public Boolean convert(
        ConsumerRecordContext recordContext
    ) {
        return recordContext.getExceptionCounter() >= recordContext.getDlqRetryConsumer().getDelays().size();
    }

}