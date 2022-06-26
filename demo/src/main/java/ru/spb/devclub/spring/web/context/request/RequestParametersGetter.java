package ru.spb.devclub.spring.web.context.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestParametersGetter {

    private final ExecutorService executor;

    @Qualifier("executorWithRequestContext")
    private final ExecutorService executorWithRequestContext;

    public String getParams() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            log.info("Getting params with context");
            StringJoiner builder = new StringJoiner("\n");
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            request.getParameterMap().forEach((n, v) -> builder.add(n + "=" + Arrays.toString(v)));
            return builder.toString();
        } else {
            log.info("Getting params without context");
            return "none";
        }
    }

    @Async
    public Future<String> getParamsAsync() {
        return new AsyncResult<>(getParams());
    }

    @Async("asyncTaskExecutorWithRequestContext")
    public Future<String> getParamsAsyncWithContext() {
        return new AsyncResult<>(getParams());
    }

    public Future<String> getParamsFromExecutor() {
        return executor.submit(this::getParams);
    }

    public Future<String> getParamsFromExecutorWithContext() {
        return executorWithRequestContext.submit(this::getParams);
    }

}
