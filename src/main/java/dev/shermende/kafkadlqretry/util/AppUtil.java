package dev.shermende.kafkadlqretry.util;

import dev.shermende.kafkadlqretry.model.DlqRetryConsumer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.util.Optional;

@Slf4j
@UtilityClass
public class AppUtil {

    public Integer extractCounter(
        DlqRetryConsumer consumer,
        ConsumerRecord<Object, Object> record
    ) {
        final Header header = record.headers().lastHeader(consumer.getRetryCounterHeader());
        return Optional.ofNullable(header).map(Header::value).map(String::new).map(AppUtil::parse).orElse(0);
    }

    private Integer parse(
        String value
    ) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

}
