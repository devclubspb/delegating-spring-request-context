package ru.spb.devclub.spring.web.context.request.concurrent;


import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * <p>
 * Wraps a delegate {@link Runnable} with logic for setting up a {@link RequestAttributes}
 * before invoking the delegate {@link Runnable} and then removing the
 * {@link RequestAttributes} after the delegate has completed.
 * </p>
 * <p>
 * If there is a {@link RequestContextHolder} that already exists, it will be restored after
 * the {@link #run()} method is invoked.
 * </p>
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/DelegatingSecurityContextRunnable.html">
 * DelegatingSecurityContextRunnable
 * </a>
 * @since 1.0
 */
public final class DelegatingRequestContextRunnable implements Runnable {

    private final Runnable delegate;

    /**
     * The {@link RequestAttributes} that the delegate {@link Runnable} will be ran as.
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
     * Creates a new {@link DelegatingRequestContextRunnable}
     * with a specific {@link RequestAttributes} and {@code inheritable}.
     *
     * @param delegate          the delegate {@link Runnable} to run with the specified
     *                          {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Runnable}. Cannot be null.
     * @param inheritable       the {@code inheritable} in
     *                          {@link RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)}
     */
    public DelegatingRequestContextRunnable(Runnable delegate, RequestAttributes requestAttributes, boolean inheritable) {
        Assert.notNull(delegate, "delegate cannot be null");
        Assert.notNull(requestAttributes, "requestAttributes cannot be null");
        this.delegate = delegate;
        this.delegateRequestAttributes = requestAttributes;
        this.inheritable = inheritable;
    }

    /**
     * Creates a new {@link DelegatingRequestContextRunnable}
     * with a specific {@link RequestAttributes}.
     *
     * @param delegate          the delegate {@link Runnable} to run with the specified
     *                          {@link RequestAttributes}. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to establish for the delegate
     *                          {@link Runnable}. Cannot be null.
     */
    public DelegatingRequestContextRunnable(Runnable delegate, RequestAttributes requestAttributes) {
        this(delegate, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextRunnable}
     * with the {@link RequestAttributes} from the {@link RequestContextHolder} and {@code inheritable}.
     *
     * @param delegate    the delegate {@link Runnable} to run under the current
     *                    {@link RequestAttributes}. Cannot be null.
     * @param inheritable the {@code inheritable} in
     *                    {@link RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)}
     */
    public DelegatingRequestContextRunnable(Runnable delegate, boolean inheritable) {
        this(delegate, RequestContextHolder.getRequestAttributes(), inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextRunnable}
     * with the {@link RequestAttributes} from the {@link RequestContextHolder}.
     *
     * @param delegate the delegate {@link Runnable} to run under the current
     *                 {@link RequestAttributes}. Cannot be null.
     */
    public DelegatingRequestContextRunnable(Runnable delegate) {
        this(delegate, false);
    }

    @Override
    public void run() {
        this.originalRequestAttributes = RequestContextHolder.getRequestAttributes();
        try {
            RequestContextHolder.setRequestAttributes(this.delegateRequestAttributes, inheritable);
            this.delegate.run();
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
     * Factory method for creating a {@link DelegatingRequestContextRunnable}.
     *
     * @param delegate          the original {@link Runnable} that will be delegated to after
     *                          establishing a {@link RequestAttributes} on the {@link RequestContextHolder}. Cannot
     *                          have null.
     * @param requestAttributes the {@link RequestAttributes} to establish before invoking the
     *                          delegate {@link Runnable}. If null, the current {@link RequestAttributes} from the
     *                          {@link RequestContextHolder} will be used.
     * @return created {@link DelegatingRequestContextRunnable}
     */
    public static Runnable create(Runnable delegate, RequestAttributes requestAttributes) {
        Assert.notNull(delegate, "delegate cannot be null");
        return (requestAttributes != null)
                ? new DelegatingRequestContextRunnable(delegate, requestAttributes)
                : new DelegatingRequestContextRunnable(delegate);
    }

    /**
     * Factory method for creating a {@link DelegatingRequestContextRunnable} with {@code inheritable=true}.
     *
     * @param delegate          the original {@link Runnable} that will be delegated to after
     *                          establishing a {@link RequestAttributes} on the {@link RequestContextHolder}. Cannot
     *                          have null.
     * @param requestAttributes the {@link RequestAttributes} to establish before invoking the
     *                          delegate {@link Runnable}. If null, the current {@link RequestAttributes} from the
     *                          {@link RequestContextHolder} will be used.
     * @return created {@link DelegatingRequestContextRunnable}
     */
    public static Runnable createInheritable(Runnable delegate, RequestAttributes requestAttributes) {
        Assert.notNull(delegate, "delegate cannot be null");
        return (requestAttributes != null)
                ? new DelegatingRequestContextRunnable(delegate, requestAttributes, true)
                : new DelegatingRequestContextRunnable(delegate, true);
    }

}

