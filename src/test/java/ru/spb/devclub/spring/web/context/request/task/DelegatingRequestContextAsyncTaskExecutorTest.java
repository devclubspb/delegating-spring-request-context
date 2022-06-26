package ru.spb.devclub.spring.web.context.request.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncTaskExecutor;
import ru.spb.devclub.spring.web.context.request.BaseRequestContextHolderTest;
import ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class DelegatingRequestContextAsyncTaskExecutorTest extends BaseRequestContextHolderTest {

    AsyncTaskExecutor executor = new TestAsyncTaskExecutor();
    Runnable runnable = () -> {};
    Callable<?> callable = () -> null;

    protected AsyncTaskExecutor wrap(AsyncTaskExecutor executor) {
        return new DelegatingRequestContextAsyncTaskExecutor(executor);
    }

    @Test
    void should_wrap_runnable_onExecute() {
        wrap(executor).execute(runnable);
    }

    @Test
    void should_wrap_runnable_onExecuteWithTimeout() {
        wrap(executor).execute(runnable, 1);
    }

    @Test
    void should_wrap_runnable_onSubmit() {
        wrap(executor).submit(runnable);
    }

    @Test
    void should_wrap_callable_onExecute() {
        wrap(executor).submit(callable);
    }

    protected static class TestAsyncTaskExecutor
            extends DelegatingRequestContextTaskExecutorTest.TestTaskExecutor
            implements AsyncTaskExecutor {
        @Override
        public void execute(Runnable runnable, long l) {
            execute(runnable);
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            execute(runnable);
            return null;
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            Assertions.assertInstanceOf(DelegatingRequestContextCallable.class, callable);
            return null;
        }
    }

}