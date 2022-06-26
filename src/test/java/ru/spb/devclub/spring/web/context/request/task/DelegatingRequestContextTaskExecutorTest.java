package ru.spb.devclub.spring.web.context.request.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import ru.spb.devclub.spring.web.context.request.BaseRequestContextHolderTest;
import ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable;

class DelegatingRequestContextTaskExecutorTest extends BaseRequestContextHolderTest {

    TaskExecutor executor = new TestTaskExecutor();
    Runnable runnable = () -> {};

    protected TaskExecutor wrap(TaskExecutor executor) {
        return new DelegatingRequestContextTaskExecutor(executor);
    }

    @Test
    void should_wrap_runnable_onExecute() {
        wrap(executor).execute(runnable);
    }

    protected static class TestTaskExecutor implements TaskExecutor {
        @Override
        public void execute(Runnable command) {
            Assertions.assertInstanceOf(DelegatingRequestContextRunnable.class, command);
        }
    }

}