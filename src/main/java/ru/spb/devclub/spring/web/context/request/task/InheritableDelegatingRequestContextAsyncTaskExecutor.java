package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Callable;

/**
 * An {@link AsyncTaskExecutor} which wraps each {@link Runnable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
 * with {@code inheritable=true}
 * and each {@link Callable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextCallable}
 * with {@code inheritable=true}.
 *
 * @see org.springframework.web.context.request.RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
 * @since 1.0
 */
public class InheritableDelegatingRequestContextAsyncTaskExecutor
        extends DelegatingRequestContextAsyncTaskExecutor
        implements AsyncTaskExecutor {

    /**
     * Creates a new {@link InheritableDelegatingRequestContextAsyncTaskExecutor} that uses the
     * specified {@link RequestAttributes}.
     *
     * @param delegateAsyncTaskExecutor the {@link AsyncTaskExecutor} to delegate to.
     *                                  Cannot be null.
     * @param requestAttributes         the {@link RequestAttributes} to use for each
     *                                  {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
     *                                  and
     *                                  {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextCallable}
     */
    public InheritableDelegatingRequestContextAsyncTaskExecutor(
            AsyncTaskExecutor delegateAsyncTaskExecutor,
            RequestAttributes requestAttributes) {
        super(delegateAsyncTaskExecutor, requestAttributes, true);
    }

    /**
     * Creates a new {@link InheritableDelegatingRequestContextAsyncTaskExecutor} that uses the
     * current {@link RequestAttributes}.
     *
     * @param delegateAsyncTaskExecutor the {@link AsyncTaskExecutor} to delegate to.
     *                                  Cannot be null.
     */
    public InheritableDelegatingRequestContextAsyncTaskExecutor(AsyncTaskExecutor delegateAsyncTaskExecutor) {
        this(delegateAsyncTaskExecutor, null);
    }

}
