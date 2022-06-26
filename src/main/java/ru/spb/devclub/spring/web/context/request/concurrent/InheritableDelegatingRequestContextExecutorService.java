package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * An {@link ExecutorService} which wraps each {@link Runnable} in a
 * {@link DelegatingRequestContextRunnable} with {@code inheritable=true} and each {@link Callable} in a
 * {@link DelegatingRequestContextCallable} with {@code inheritable=true}.
 *
 * @see org.springframework.web.context.request.RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
 * @since 1.0
 */
public class InheritableDelegatingRequestContextExecutorService
        extends DelegatingRequestContextExecutorService
        implements ExecutorService {

    /**
     * Creates a new {@link InheritableDelegatingRequestContextExecutorService} that uses the
     * specified {@link RequestAttributes}.
     *
     * @param delegateExecutorService the {@link ExecutorService} to delegate to. Cannot
     *                                be null.
     * @param requestAttributes       the {@link RequestAttributes} to use for each
     *                                {@link DelegatingRequestContextRunnable} and each
     *                                {@link DelegatingRequestContextCallable}.
     */
    public InheritableDelegatingRequestContextExecutorService(ExecutorService delegateExecutorService, RequestAttributes requestAttributes) {
        super(delegateExecutorService, requestAttributes, true);
    }

    /**
     * Creates a new {@link InheritableDelegatingRequestContextExecutorService} that uses the
     * current {@link RequestAttributes} from the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link ExecutorService} to delegate to. Cannot be null.
     */
    public InheritableDelegatingRequestContextExecutorService(ExecutorService delegate) {
        this(delegate, null);
    }

}
