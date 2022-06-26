package ru.spb.devclub.spring.web.context.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfiguration {

    @Bean
    @Primary
    public ExecutorService executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean("executorWithRequestContext")
    public ExecutorService executorWithRequestContext() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return new DelegatingRequestContextExecutorService(executor);
    }

}
