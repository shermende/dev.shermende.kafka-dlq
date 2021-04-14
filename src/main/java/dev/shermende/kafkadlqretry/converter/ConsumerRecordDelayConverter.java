package dev.shermende.kafkadlqretry.converter;

import dev.shermende.kafkadlqretry.model.ConsumerRecordContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Delay resolver
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerRecordDelayConverter implements Converter<ConsumerRecordContext, Long> {

    @NotNull
    @Override
    public Long convert(
        ConsumerRecordContext recordContext
    ) {
        val difference = (System.currentTimeMillis() - recordContext.getRecord().timestamp());
        val delay = recordContext.getDlqRetryConsumer().getDelays().get(recordContext.getExceptionCounter());
        return difference >= delay ? 0 : delay - difference;
    }

}