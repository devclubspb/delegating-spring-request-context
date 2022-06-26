package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * An internal support class that wraps {@link Callable} with
 * {@link DelegatingRequestContextCallable} and {@link Runnable} with
 * {@link DelegatingRequestContextRunnable}
 *
 * @since 1.0
 */
abstract class AbstractDelegatingRequestContextSupport {

    private final RequestAttributes requestAttributes;

    /**
     * @see RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
     */
    private final boolean inheritable;

    /**
     * Creates a new {@link AbstractDelegatingRequestContextSupport} that uses the
     * specified {@link RequestAttributes}.
     *
     * @param requestAttributes the {@link RequestAttributes} to use for each
     *                          {@link DelegatingRequestContextRunnable} and each
     *                          {@link DelegatingRequestContextCallable} or null to default to the current
     *                          {@link RequestAttributes}.
     * @param inheritable       the {@code inheritable} in
     *                          {@link RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)}
     */
    AbstractDelegatingRequestContextSupport(RequestAttributes requestAttributes, boolean inheritable) {
        this.requestAttributes = requestAttributes;
        this.inheritable = inheritable;
    }

    /**
     * Wraps a {@link Runnable} in {@link DelegatingRequestContextRunnable}
     *
     * @param delegate the original {@link Runnable}
     * @return wrapped {@link Runnable}
     */
    protected final Runnable wrap(Runnable delegate) {
        return inheritable
                ? DelegatingRequestContextRunnable.createInheritable(delegate, this.requestAttributes)
                : DelegatingRequestContextRunnable.create(delegate, this.requestAttributes);
    }

    /**
     * Wraps a {@link Callable} in {@link DelegatingRequestContextCallable}
     *
     * @param delegate the original {@link Callable}
     * @param <T>      the result type of {@link Callable}
     * @return wrapped {@link Callable}
     */
    protected final <T> Callable<T> wrap(Callable<T> delegate) {
        return inheritable
                ? DelegatingRequestContextCallable.createInheritable(delegate, this.requestAttributes)
                : DelegatingRequestContextCallable.create(delegate, this.requestAttributes);
    }

}
