package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.TaskExecutor;

class InheritableDelegatingRequestContextTaskExecutorTest
        extends DelegatingRequestContextTaskExecutorTest {

    @Override
    protected TaskExecutor wrap(TaskExecutor executor) {
        return new InheritableDelegatingRequestContextTaskExecutor(executor);
    }

}