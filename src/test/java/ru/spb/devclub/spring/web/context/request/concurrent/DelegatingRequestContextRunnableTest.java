package ru.spb.devclub.spring.web.context.request.concurrent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
class DelegatingRequestContextRunnableTest {

    static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    static AtomicReference<RequestAttributes> HOLDER = new AtomicReference<>();

    @Mock
    RequestAttributes mockedAttrs;

    @AfterAll
    static void afterAll() {
        EXECUTOR.shutdownNow();
    }

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(mockedAttrs);
        HOLDER.set(null);
    }

    void execute(Runnable runnable) {
        try {
            EXECUTOR.submit(runnable).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AssertionError(e);
        }
    }

    void holdAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        HOLDER.set(attrs);
    }

    @Test
    void should_hold_attrs() {
        RequestAttributes before = RequestContextHolder.getRequestAttributes();
        execute(DelegatingRequestContextRunnable.create(this::holdAttributes, null));
        RequestAttributes after = RequestContextHolder.getRequestAttributes();
        Assertions.assertSame(before, after, "Before and after");
        RequestAttributes saved = HOLDER.get();
        Assertions.assertSame(before, saved, "Before and saved");
    }

    @Test
    void should_hold_customAttrs() {
        RequestAttributes before = RequestContextHolder.getRequestAttributes();
        RequestAttributes custom = Mockito.mock(RequestAttributes.class);
        execute(DelegatingRequestContextRunnable.create(this::holdAttributes, custom));
        RequestAttributes after = RequestContextHolder.getRequestAttributes();
        Assertions.assertSame(before, after, "Before and after");
        RequestAttributes saved = HOLDER.get();
        Assertions.assertSame(custom, saved, "Custom and saved");
    }

    @Test
    void should_hold_innerAttrs() {
        RequestAttributes before = RequestContextHolder.getRequestAttributes();
        RequestAttributes innerBefore = Mockito.mock(RequestAttributes.class);
        AtomicReference<RequestAttributes> innerHolder = new AtomicReference<>();
        Runnable runnable = DelegatingRequestContextRunnable.create(this::holdAttributes, null);
        execute(() -> {
            RequestContextHolder.setRequestAttributes(innerBefore);
            runnable.run();
            innerHolder.set(RequestContextHolder.getRequestAttributes());
        });
        RequestAttributes after = RequestContextHolder.getRequestAttributes();
        Assertions.assertSame(before, after, "Before and after");
        RequestAttributes saved = HOLDER.get();
        Assertions.assertSame(before, saved, "Before and saved");
        RequestAttributes innerAfter = innerHolder.get();
        Assertions.assertSame(innerBefore, innerAfter, "InnerBefore and innerAfter");
    }

    @Test
    void should_toString() {
        Runnable mock = Mockito.mock(Runnable.class);
        String expected = "MyName";
        Mockito.when(mock.toString()).thenReturn(expected);
        String actual = new DelegatingRequestContextRunnable(mock).toString();
        Assertions.assertEquals(expected, actual, "ToString");
    }

}