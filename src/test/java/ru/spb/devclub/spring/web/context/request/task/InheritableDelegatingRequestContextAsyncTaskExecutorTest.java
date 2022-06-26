package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.AsyncTaskExecutor;

class InheritableDelegatingRequestContextAsyncTaskExecutorTest
        extends DelegatingRequestContextAsyncTaskExecutorTest {

    @Override
    protected AsyncTaskExecutor wrap(AsyncTaskExecutor executor) {
        return new InheritableDelegatingRequestContextAsyncTaskExecutor(executor);
    }

}