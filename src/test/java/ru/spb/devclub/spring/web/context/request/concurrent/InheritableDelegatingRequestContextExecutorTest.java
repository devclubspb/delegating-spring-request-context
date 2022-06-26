package ru.spb.devclub.spring.web.context.request.concurrent;

import java.util.concurrent.Executor;

class InheritableDelegatingRequestContextExecutorTest
        extends DelegatingRequestContextExecutorTest {

    @Override
    protected Executor wrap(Executor executor) {
        return new InheritableDelegatingRequestContextExecutor(executor);
    }

}