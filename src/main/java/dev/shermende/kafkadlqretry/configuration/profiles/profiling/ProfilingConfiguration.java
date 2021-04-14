package dev.shermende.kafkadlqretry.configuration.profiles.profiling;

import dev.shermende.support.spring.aop.logging.LoggingAspect;
import dev.shermende.support.spring.aop.profiling.ProfilingAspect;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Controls for remote management of profiling and logging
 */
@Slf4j
@Configuration
@Profile({"profiling"})
public class ProfilingConfiguration {

    @Bean
    public JmxControl profilingJmxControl() {
        return new ToggleJmxControlImpl(true);
    }

    @Bean
    public JmxControl loggingJmxControl() {
        return new ToggleJmxControlImpl(true);
    }

    @Bean
    public ProfilingAspect profilingAspect(
        @Qualifier("profilingJmxControl") JmxControl jmxControl
    ) {
        return new ProfilingAspect(jmxControl);
    }

    @Bean
    public LoggingAspect loggingAspect(
        @Qualifier("loggingJmxControl") JmxControl jmxControl
    ) {
        return new LoggingAspect(jmxControl);
    }

}