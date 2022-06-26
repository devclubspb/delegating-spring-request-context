package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link ScheduledExecutorService} which wraps each {@link Runnable} in a
 * {@link DelegatingRequestContextRunnable} and each {@link Callable} in a
 * {@link DelegatingRequestContextCallable}.
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/DelegatingSecurityContextScheduledExecutorService.html">
 * DelegatingSecurityContextScheduledExecutorService
 * </a>
 * @since 1.0
 */
public class DelegatingRequestContextScheduledExecutorService extends DelegatingRequestContextExecutorService
        implements ScheduledExecutorService {

    /**
     * {@inheritDoc}
     */
    protected DelegatingRequestContextScheduledExecutorService(
            ScheduledExecutorService delegateScheduledExecutorService,
            RequestAttributes requestAttributes,
            boolean inheritable) {
        super(delegateScheduledExecutorService, requestAttributes, inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextScheduledExecutorService} that uses
     * the specified {@link RequestAttributes}.
     *
     * @param delegateScheduledExecutorService the {@link ScheduledExecutorService} to
     *                                         delegate to. Cannot be null.
     * @param requestAttributes                the {@link RequestAttributes} to use for each
     *                                         {@link DelegatingRequestContextRunnable} and each
     *                                         {@link DelegatingRequestContextCallable}.
     */
    public DelegatingRequestContextScheduledExecutorService(
            ScheduledExecutorService delegateScheduledExecutorService,
            RequestAttributes requestAttributes) {
        this(delegateScheduledExecutorService, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextScheduledExecutorService} that uses
     * the current {@link RequestAttributes} from the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link ScheduledExecutorService} to delegate to. Cannot be
     *                 null.
     */
    public DelegatingRequestContextScheduledExecutorService(ScheduledExecutorService delegate) {
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
