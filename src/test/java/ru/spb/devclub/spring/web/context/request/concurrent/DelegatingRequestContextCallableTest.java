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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
class DelegatingRequestContextCallableTest {

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

    void execute(Callable<?> callable) {
        try {
            EXECUTOR.submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AssertionError(e);
        }
    }

    Void holdAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        HOLDER.set(attrs);
        return null;
    }

    @Test
    void should_hold_attrs() {
        RequestAttributes before = RequestContextHolder.getRequestAttributes();
        execute(DelegatingRequestContextCallable.create(this::holdAttributes, null));
        RequestAttributes after = RequestContextHolder.getRequestAttributes();
        Assertions.assertSame(before, after, "Before and after");
        RequestAttributes saved = HOLDER.get();
        Assertions.assertSame(before, saved, "Before and saved");
    }

    @Test
    void should_hold_customAttrs() {
        RequestAttributes before = RequestContextHolder.getRequestAttributes();
        RequestAttributes custom = Mockito.mock(RequestAttributes.class);
        execute(DelegatingRequestContextCallable.create(this::holdAttributes, custom));
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
        Callable<Void> callable = DelegatingRequestContextCallable.create(this::holdAttributes, null);
        execute(() -> {
            RequestContextHolder.setRequestAttributes(innerBefore);
            callable.call();
            innerHolder.set(RequestContextHolder.getRequestAttributes());
            return null;
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
        Callable<?> mock = Mockito.mock(Callable.class);
        String expected = "MyName";
        Mockito.when(mock.toString()).thenReturn(expected);
        String actual = new DelegatingRequestContextCallable<>(mock).toString();
        Assertions.assertEquals(expected, actual, "ToString");
    }

}