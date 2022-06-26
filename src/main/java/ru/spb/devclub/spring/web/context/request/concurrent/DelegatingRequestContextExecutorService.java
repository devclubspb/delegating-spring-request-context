package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An {@link ExecutorService} which wraps each {@link Runnable} in a
 * {@link DelegatingRequestContextRunnable} and each {@link Callable} in a
 * {@link DelegatingRequestContextCallable}.
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/DelegatingSecurityContextExecutorService.html">
 * DelegatingSecurityContextExecutorService
 * </a>
 * @since 1.0
 */
public class DelegatingRequestContextExecutorService extends DelegatingRequestContextExecutor
        implements ExecutorService {

    /**
     * {@inheritDoc}
     */
    protected DelegatingRequestContextExecutorService(
            ExecutorService delegateExecutorService,
            RequestAttributes requestAttributes,
            boolean inheritable) {
        super(delegateExecutorService, requestAttributes, inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextExecutorService} that uses the
     * specified {@link RequestAttributes}.
     *
     * @param delegateExecutorService the {@link ExecutorService} to delegate to. Cannot
     *                                be null.
     * @param requestAttributes       the {@link RequestAttributes} to use for each
     *                                {@link DelegatingRequestContextRunnable} and each
     *                                {@link DelegatingRequestContextCallable}.
     */
    public DelegatingRequestContextExecutorService(
            ExecutorService delegateExecutorService,
            RequestAttributes requestAttributes) {
        this(delegateExecutorService, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextExecutorService} that uses the
     * current {@link RequestAttributes} from the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link ExecutorService} to delegate to. Cannot be null.
     */
    public DelegatingRequestContextExecutorService(ExecutorService delegate) {
        this(delegate, null);
    }

    @Override
    public final void shutdown() {
        getDelegate().shutdown();
    }

    @Override
    public final List<Runnable> shutdownNow() {
        return getDelegate().shutdownNow();
    }

    @Override
    public final boolean isShutdown() {
        return getDelegate().isShutdown();
    }

    @Override
    public final boolean isTerminated() {
        return getDelegate().isTerminated();
    }

    @Override
    public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return getDelegate().awaitTermination(timeout, unit);
    }

    @Override
    public final <T> Future<T> submit(Callable<T> task) {
        return getDelegate().submit(wrap(task));
    }

    @Override
    public final <T> Future<T> submit(Runnable task, T result) {
        return getDelegate().submit(wrap(task), result);
    }

    @Override
    public final Future<?> submit(Runnable task) {
        return getDelegate().submit(wrap(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        tasks = createTasks(tasks);
        return getDelegate().invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        tasks = createTasks(tasks);
        return getDelegate().invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        tasks = createTasks(tasks);
        return getDelegate().invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        tasks = createTasks(tasks);
        return getDelegate().invokeAny(tasks, timeout, unit);
    }

    private <T> Collection<Callable<T>> createTasks(Collection<? extends Callable<T>> tasks) {
        if (tasks == null) {
            return null;
        }
        List<Callable<T>> results = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            results.add(wrap(task));
        }
        return results;
    }

    private ExecutorService getDelegate() {
        return (ExecutorService) getDelegateExecutor();
    }

}
