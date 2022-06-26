package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * An {@link AsyncTaskExecutor} which wraps each {@link Runnable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
 * and each {@link Callable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextCallable}.
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/task/DelegatingSecurityContextAsyncTaskExecutor.html">
 * DelegatingSecurityContextAsyncTaskExecutor
 * </a>
 * @since 1.0
 */
public class DelegatingRequestContextAsyncTaskExecutor
        extends DelegatingRequestContextTaskExecutor
        implements AsyncTaskExecutor {

    /**
     * {@inheritDoc}
     */
    protected DelegatingRequestContextAsyncTaskExecutor(
            AsyncTaskExecutor delegateAsyncTaskExecutor,
            RequestAttributes requestAttributes,
            boolean inheritable) {
        super(delegateAsyncTaskExecutor, requestAttributes, inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextAsyncTaskExecutor} that uses the
     * specified {@link RequestAttributes}.
     *
     * @param delegateAsyncTaskExecutor the {@link AsyncTaskExecutor} to delegate to.
     *                                  Cannot be null.
     * @param requestAttributes         the {@link RequestAttributes} to use for each
     *                                  {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
     *                                  and
     *                                  {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextCallable}
     */
    public DelegatingRequestContextAsyncTaskExecutor(
            AsyncTaskExecutor delegateAsyncTaskExecutor,
            RequestAttributes requestAttributes) {
        this(delegateAsyncTaskExecutor, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextAsyncTaskExecutor} that uses the
     * current {@link RequestAttributes}.
     *
     * @param delegateAsyncTaskExecutor the {@link AsyncTaskExecutor} to delegate to.
     *                                  Cannot be null.
     */
    public DelegatingRequestContextAsyncTaskExecutor(AsyncTaskExecutor delegateAsyncTaskExecutor) {
        this(delegateAsyncTaskExecutor, null);
    }

    @Override
    public final void execute(Runnable task, long startTimeout) {
        getDelegate().execute(wrap(task), startTimeout);
    }

    @Override
    public final Future<?> submit(Runnable task) {
        return getDelegate().submit(wrap(task));
    }

    @Override
    public final <T> Future<T> submit(Callable<T> task) {
        return getDelegate().submit(wrap(task));
    }

    private AsyncTaskExecutor getDelegate() {
        return (AsyncTaskExecutor) getDelegateExecutor();
    }

}
