package ru.spb.devclub.spring.web.context.request.concurrent;

import java.util.concurrent.ExecutorService;

class InheritableDelegatingRequestContextExecutorServiceTest
        extends DelegatingRequestContextExecutorServiceTest {

    @Override
    protected ExecutorService wrap(ExecutorService executor) {
        return new InheritableDelegatingRequestContextExecutorService(executor);
    }

}