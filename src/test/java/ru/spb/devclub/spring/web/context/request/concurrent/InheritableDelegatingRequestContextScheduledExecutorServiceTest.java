package ru.spb.devclub.spring.web.context.request.concurrent;

import java.util.concurrent.ScheduledExecutorService;

class InheritableDelegatingRequestContextScheduledExecutorServiceTest
        extends DelegatingRequestContextScheduledExecutorServiceTest {

    @Override
    protected ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        return new InheritableDelegatingRequestContextScheduledExecutorService(executor);
    }

}