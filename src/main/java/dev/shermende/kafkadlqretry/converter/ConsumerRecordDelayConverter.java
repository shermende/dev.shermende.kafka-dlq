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
public class ConsumerRecordDelayConverter implements Converter<ConsumerRecordContext, Long> {

    @NotNull
    @Override
    public Long convert(
        ConsumerRecordContext recordContext
    ) {
        final long difference = (System.currentTimeMillis() - recordContext.getRecord().timestamp());
        final long delay = recordContext.getDlqRetryConsumer().getDelays().get(recordContext.getCounter());
        return difference >= delay ? 0 : delay - difference;
    }

}