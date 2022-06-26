package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * <p>
 * Wraps a delegate {@link Callable} with logic for setting up a {@link RequestAttributes}
 * before invoking the delegate {@link Callable} and then removing the
 * {@link RequestAttributes} after the delegate has completed.
 * </p>
 * <p>
 * If there is a {@link RequestAttributes} that already exists, it will be restored after
 * the {@link #call()} method is invoked.
 * </p>
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/DelegatingSecurityContextCallable.html">
 * DelegatingSecurityContextCallable
 * </a>
 * @since 1.0
 */
public final class DelegatingRequestContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;

    /**
     * The {@link RequestAttributes} that the delegate {@link Callable} will be ran as.
     */
    private final RequestAttributes delegateRequestAttributes;

    /**
     * @see RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
     */
    private final boolean inheritable;

    /**
     * The {@link RequestAttributes} that was on the {@link RequestContextHolder} prior to
     * being set to the delegateSecurityContext.
     */
    private RequestAttributes originalRequestAttributes;

    /**
     * Creates a new {@link DelegatingRequestContextCallable} with a specific
     * {@link RequestAttributes}.
     *
     * @param delegate          the delegate {@link DelegatingRequestContextCallable} to run with
     *                          the specified {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Callable}. Cannot be null.
     * @param inheritable       the {@code inheritable} in
     *                          {@link RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)}
     */
    public DelegatingRequestContextCallable(Callable<V> delegate, RequestAttributes requestAttributes, boolean inheritable) {
        Assert.notNull(delegate, "delegate cannot be null");
        Assert.notNull(requestAttributes, "requestAttributes cannot be null");
        this.delegate = delegate;
        this.delegateRequestAttributes = requestAttributes;
        this.inheritable = inheritable;
    }

    /**
     * Creates a new {@link DelegatingRequestContextCallable} with a specific
     * {@link RequestAttributes}.
     *
     * @param delegate          the delegate {@link DelegatingRequestContextCallable} to run with
     *                          the specified {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Callable}. Cannot be null.
     */
    public DelegatingRequestContextCallable(Callable<V> delegate, RequestAttributes requestAttributes) {
        this(delegate, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextCallable} with the
     * {@link RequestAttributes} from the {@link RequestContextHolder}.
     *
     * @param delegate    the delegate {@link Callable} to run under the current
     *                    {@link RequestAttributes}. Cannot be null.
     * @param inheritable the {@code inheritable} in
     *                    {@link RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)}
     */
    public DelegatingRequestContextCallable(Callable<V> delegate, boolean inheritable) {
        this(delegate, RequestContextHolder.getRequestAttributes(), inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextCallable} with the
     * {@link RequestAttributes} from the {@link RequestContextHolder}.
     *
     * @param delegate the delegate {@link Callable} to run under the current
     *                 {@link RequestAttributes}. Cannot be null.
     */
    public DelegatingRequestContextCallable(Callable<V> delegate) {
        this(delegate, false);
    }

    @Override
    public V call() throws Exception {
        this.originalRequestAttributes = RequestContextHolder.getRequestAttributes();
        try {
            RequestContextHolder.setRequestAttributes(this.delegateRequestAttributes, inheritable);
            return this.delegate.call();
        } finally {
            if (this.originalRequestAttributes == null) {
                RequestContextHolder.resetRequestAttributes();
            } else {
                RequestContextHolder.setRequestAttributes(this.originalRequestAttributes, inheritable);
            }
            this.originalRequestAttributes = null;
        }
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }

    /**
     * Creates a {@link DelegatingRequestContextCallable} and with the given
     * {@link Callable} and {@link RequestAttributes}, but if the requestAttributes is null
     * will defaults to the current {@link RequestAttributes} on the
     * {@link RequestContextHolder}
     *
     * @param delegate          the delegate {@link DelegatingRequestContextCallable} to run with
     *                          the specified {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Callable}.
     *                          If null, defaults to {@link RequestContextHolder#getRequestAttributes()}
     * @return created {@link DelegatingRequestContextCallable}
     * @param <V> the result type of {@link Callable}
     */
    public static <V> Callable<V> create(Callable<V> delegate, RequestAttributes requestAttributes) {
        Assert.notNull(delegate, "delegate cannot be null");
        return (requestAttributes != null) ? new DelegatingRequestContextCallable<>(delegate, requestAttributes) : new DelegatingRequestContextCallable<>(delegate);
    }

    /**
     * Creates a {@link DelegatingRequestContextCallable} with {@code inheritable=true} and with the given
     * {@link Callable} and {@link RequestAttributes}, but if the requestAttributes is null
     * will defaults to the current {@link RequestAttributes} on the
     * {@link RequestContextHolder}
     *
     * @param delegate          the delegate {@link DelegatingRequestContextCallable} to run with
     *                          the specified {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Callable}.
     *                          If null, defaults to {@link RequestContextHolder#getRequestAttributes()}
     * @param <V>               the result type of {@link Callable}
     * @return created {@link DelegatingRequestContextCallable}
     */
    public static <V> Callable<V> createInheritable(Callable<V> delegate, RequestAttributes requestAttributes) {
        Assert.notNull(delegate, "delegate cannot be null");
        return (requestAttributes != null) ? new DelegatingRequestContextCallable<>(delegate, requestAttributes, true) : new DelegatingRequestContextCallable<>(delegate, true);
    }

}
