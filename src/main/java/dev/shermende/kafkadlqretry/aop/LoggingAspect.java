package dev.shermende.kafkadlqretry.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    @Around("@annotation(dev.shermende.kafkadlqretry.aop.annotation.Logging)")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            final Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
            final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            final Method method = signature.getMethod();
            final String logRow = Optional.ofNullable(proceedingJoinPoint.getArgs())
                .map(objects -> Arrays.stream(objects).filter(Objects::nonNull)
                    .map(Object::toString).map(this::adjust).collect(Collectors.joining(",")))
                .orElse(null);
            log.debug("[Logging before] [{}#{}] [Args:{}]", aClass.getSimpleName(), method.getName(), logRow);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        final Object proceed = proceedingJoinPoint.proceed();
        try {
            final Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
            final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            final Method method = signature.getMethod();
            final String logRow = Optional.ofNullable(proceed).map(Object::toString).map(this::adjust).orElse(null);
            log.debug("[Logging after] [{}#{}] [Result:{}]", aClass.getSimpleName(), method.getName(), logRow);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return proceed;
    }

    private String adjust(String s) {
        if (s.length() > 512) return s.substring(0, 511);
        return s;
    }

}
