package ru.spb.devclub.spring.web.context.request;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.spb.devclub.spring.web.context.request.task.DelegatingRequestContextAsyncTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

    @Bean
    @Primary
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor("simple-");
    }

    @Bean("asyncTaskExecutorWithRequestContext")
    public AsyncTaskExecutor asyncTaskExecutorWithRequestContext() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("withRequestContext-");
        return new DelegatingRequestContextAsyncTaskExecutor(executor);
    }

}
