package ru.spb.devclub.spring.web.context.request.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.spb.devclub.spring.web.context.request.BaseRequestContextHolderTest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class DelegatingRequestContextExecutorServiceTest extends BaseRequestContextHolderTest {

    ExecutorService executor = new TestExecutorService();
    Runnable runnable = () -> {
    };
    Callable<?> callable = () -> null;

    protected ExecutorService wrap(ExecutorService executor) {
        return new DelegatingRequestContextExecutorService(executor);
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
    void should_shutdown() {
        ExecutorService mock = Mockito.mock(ExecutorService.class);
        wrap(mock).shutdown();
        Mockito.verify(mock).shutdown();
    }

    @Test
    void should_shutdownNow() {
        ExecutorService mock = Mockito.mock(ExecutorService.class);
        wrap(mock).shutdownNow();
        Mockito.verify(mock).shutdownNow();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void should_isShutdown() {
        ExecutorService mock = Mockito.mock(ExecutorService.class);
        wrap(mock).isShutdown();
        Mockito.verify(mock).isShutdown();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void should_awaitTermination() throws InterruptedException {
        ExecutorService mock = Mockito.mock(ExecutorService.class);
        wrap(mock).awaitTermination(1L, TimeUnit.SECONDS);
        Mockito.verify(mock).awaitTermination(Mockito.eq(1L), Mockito.eq(TimeUnit.SECONDS));
    }

    @SuppressWarnings("ConstantConditions")
    protected static class TestExecutorService
            extends DelegatingRequestContextExecutorTest.TestExecutor
            implements ExecutorService {
        @Override
        public void shutdown() {
        }

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            Assertions.assertInstanceOf(DelegatingRequestContextCallable.class, task);
            return null;
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            execute(task);
            return null;
        }

        @Override
        public Future<?> submit(Runnable task) {
            execute(task);
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
            tasks.forEach(this::submit);
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
            tasks.forEach(this::submit);
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
            tasks.forEach(this::submit);
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
            tasks.forEach(this::submit);
            return null;
        }
    }

}
