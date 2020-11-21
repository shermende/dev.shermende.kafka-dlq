package dev.shermende.kafkadlqretry.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@Profile({"profiling"})
@RequiredArgsConstructor
public class ProfilingAspect {

    @Around("@annotation(dev.shermende.kafkadlqretry.aop.annotation.Profiling)")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Class<?> aClass = proceedingJoinPoint.getTarget().getClass();
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final Method method = signature.getMethod();
        try {
            final String logRow = getArgsLog(proceedingJoinPoint);
            log.debug("[Log before] [{}#{}] [Args:{}]", aClass.getSimpleName(), method.getName(), logRow);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        final long start = System.currentTimeMillis();
        final Object proceed = proceedingJoinPoint.proceed();
        final long delta = System.currentTimeMillis() - start;
        try {
            final String logRow = getResultLog(proceed);
            log.debug("[Log after] [{}#{}] [Duration:{}] [Result:{}]", aClass.getSimpleName(), method.getName(), delta, logRow);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return proceed;
    }

    private String getArgsLog(
        ProceedingJoinPoint proceedingJoinPoint
    ) {
        return Optional.ofNullable(proceedingJoinPoint.getArgs())
            .map(objects -> Arrays.stream(objects).filter(Objects::nonNull)
                .map(Object::toString).map(this::adjust).collect(Collectors.joining(",")))
            .orElse(null);
    }

    private String getResultLog(
        Object proceed
    ) {
        return Optional.ofNullable(proceed).map(Object::toString).map(this::adjust).orElse(null);
    }

    private String adjust(String s) {
        if (s.length() > 512) return s.substring(0, 511);
        return s;
    }

}