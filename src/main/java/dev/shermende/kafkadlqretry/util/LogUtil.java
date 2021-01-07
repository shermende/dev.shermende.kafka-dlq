package dev.shermende.kafkadlqretry.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

@Slf4j
@UtilityClass
public class LogUtil {
    public String sanitize(
        @Nullable Object object
    ) {
        return StringUtils.substring(String.valueOf(object), 0, 255);
    }
}