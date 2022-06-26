package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ScheduledExecutorService} which wraps each {@link Runnable} in a
 * {@link DelegatingRequestContextRunnable} with {@code inheritable=true} and each {@link Callable} in a
 * {@link DelegatingRequestContextCallable} with {@code inheritable=true}.
 *
 * @see org.springframework.web.context.request.RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
 * @since 1.0
 */
public class InheritableDelegatingRequestContextScheduledExecutorService
        extends DelegatingRequestContextScheduledExecutorService
        implements ScheduledExecutorService {

    /**
     * Creates a new {@link InheritableDelegatingRequestContextScheduledExecutorService} that uses
     * the specified {@link RequestAttributes}.
     *
     * @param delegateScheduledExecutorService the {@link ScheduledExecutorService} to
     *                                         delegate to. Cannot be null.
     * @param requestAttributes                the {@link RequestAttributes} to use for each
     *                                         {@link DelegatingRequestContextRunnable} and each
     *                                         {@link DelegatingRequestContextCallable}.
     */
    public InheritableDelegatingRequestContextScheduledExecutorService(ScheduledExecutorService delegateScheduledExecutorService, RequestAttributes requestAttributes) {
        super(delegateScheduledExecutorService, requestAttributes, true);
    }

    /**
     * Creates a new {@link InheritableDelegatingRequestContextScheduledExecutorService} that uses
     * the current {@link RequestAttributes} from
     * the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link ScheduledExecutorService} to delegate to. Cannot be null.
     */
    public InheritableDelegatingRequestContextScheduledExecutorService(ScheduledExecutorService delegate) {
        this(delegate, null);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return getDelegate().schedule(wrap(command), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return getDelegate().schedule(wrap(callable), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return getDelegate().scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return getDelegate().scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }

    private ScheduledExecutorService getDelegate() {
        return (ScheduledExecutorService) getDelegateExecutor();
    }

}
