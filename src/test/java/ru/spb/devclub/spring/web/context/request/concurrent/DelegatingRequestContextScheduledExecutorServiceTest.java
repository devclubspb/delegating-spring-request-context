package ru.spb.devclub.spring.web.context.request.concurrent;

import org.junit.jupiter.api.Test;
import ru.spb.devclub.spring.web.context.request.BaseRequestContextHolderTest;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class DelegatingRequestContextScheduledExecutorServiceTest extends BaseRequestContextHolderTest {

    ScheduledExecutorService executor = new TestScheduledExecutorService();
    Runnable runnable = () -> {};
    Callable<?> callable = () -> null;

    protected ScheduledExecutorService wrap(ScheduledExecutorService executor) {
        return new DelegatingRequestContextScheduledExecutorService(executor);
    }

    @Test
    void should_wrap_runnable_onExecute() {
        wrap(executor).execute(runnable);
    }

    @Test
    void should_wrap_runnable_onSubmit() {
        wrap(executor).submit(runnable);
    }

    @Test
    void should_wrap_runnable_onSubmitWithResult() {
        wrap(executor).submit(runnable, null);
    }

    @Test
    void should_wrap_callable_onSubmit() {
        wrap(executor).submit(callable);
    }

    @Test
    void should_wrap_callable_onInvokeAll() throws InterruptedException {
        wrap(executor).invokeAll(List.of(callable));
    }

    @Test
    void should_wrap_callable_onInvokeAllWithTimeout() throws InterruptedException {
        wrap(executor).invokeAll(List.of(callable), 1, TimeUnit.SECONDS);
    }

    @Test
    void should_wrap_callable_onInvokeAny() throws InterruptedException, ExecutionException {
        wrap(executor).invokeAny(List.of(callable));
    }

    @Test
    void should_wrap_callable_onInvokeAnyWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        wrap(executor).invokeAny(List.of(callable), 1, TimeUnit.SECONDS);
    }

    @Test
    void should_wrap_runnable_onSchedule() {
        wrap(executor).schedule(runnable, 1, TimeUnit.SECONDS);
    }

    @Test
    void should_wrap_callable_onSchedule() {
        wrap(executor).schedule(callable, 1, TimeUnit.SECONDS);
    }

    @Test
    void should_wrap_runnable_onScheduleAtFixedRate() {
        wrap(executor).scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
    }

    @Test
    void should_wrap_runnable_onScheduleWithFixedDelay() {
        wrap(executor).scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
    }

    @SuppressWarnings("ConstantConditions")
    protected static class TestScheduledExecutorService
            extends DelegatingRequestContextExecutorServiceTest.TestExecutorService
            implements ScheduledExecutorService {
        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            execute(command);
            return null;
        }

        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            submit(callable);
            return null;
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            execute(command);
            return null;
        }

        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            execute(command);
            return null;
        }
    }

}
