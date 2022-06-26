package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} which wraps each {@link Runnable} in a {@link DelegatingRequestContextRunnable}.
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/concurrent/DelegatingSecurityContextExecutor.html">
 * DelegatingSecurityContextExecutor
 * </a>
 * @since 1.0
 */
public class DelegatingRequestContextExecutor extends AbstractDelegatingRequestContextSupport
        implements Executor {

    private final Executor delegate;

    /**
     * {@inheritDoc}
     */
    protected DelegatingRequestContextExecutor(
            Executor delegateExecutor,
            RequestAttributes requestAttributes,
            boolean inheritable) {
        super(requestAttributes, inheritable);
        Assert.notNull(delegateExecutor, "delegateExecutor cannot be null");
        this.delegate = delegateExecutor;
    }

    /**
     * Creates a new {@link DelegatingRequestContextExecutor} that uses the specified
     * {@link RequestAttributes}.
     *
     * @param delegateExecutor  the {@link Executor} to delegate to. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to use for each
     *                          {@link DelegatingRequestContextRunnable} or null to default to the current
     *                          {@link RequestAttributes}
     */
    public DelegatingRequestContextExecutor(Executor delegateExecutor, RequestAttributes requestAttributes) {
        this(delegateExecutor, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextExecutor} that uses the current {@link RequestAttributes} from
     * the {@link org.springframework.web.context.request.RequestContextHolder}
     * at the time the task is submitted.
     *
     * @param delegate the {@link Executor} to delegate to. Cannot be null.
     */
    public DelegatingRequestContextExecutor(Executor delegate) {
        this(delegate, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(Runnable task) {
        this.delegate.execute(wrap(task));
    }

    /**
     * Returns {@link #delegate}
     *
     * @return {@link #delegate}
     */
    protected final Executor getDelegateExecutor() {
        return this.delegate;
    }

}
