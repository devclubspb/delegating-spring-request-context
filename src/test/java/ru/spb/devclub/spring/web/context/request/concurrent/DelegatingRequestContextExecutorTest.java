package ru.spb.devclub.spring.web.context.request.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.devclub.spring.web.context.request.BaseRequestContextHolderTest;

import java.util.concurrent.Executor;

@ExtendWith(MockitoExtension.class)
public class DelegatingRequestContextExecutorTest extends BaseRequestContextHolderTest {

    Executor executor = new TestExecutor();
    Runnable runnable = () -> {};

    protected Executor wrap(Executor executor) {
        return new DelegatingRequestContextExecutor(executor);
    }

    @Test
    void should_wrap_runnable_onExecute() {
        wrap(executor).execute(runnable);
    }

    protected static class TestExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            Assertions.assertInstanceOf(DelegatingRequestContextRunnable.class, command);
        }
    }

}