package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingStrategyConverter implements Converter<ConsumerRecordContext, Boolean> {

    /**
     * true - handle as error
     * false - handle as retry
     */
    @NotNull
    @Override
    public Boolean convert(
        ConsumerRecordContext recordContext
    ) {
        return recordContext.getCounter() >= recordContext.getDlqRetryConsumer().getDelays().size();
    }

}