package ru.spb.devclub.spring.web.context.request.concurrent;

import org.springframework.web.context.request.RequestAttributes;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} which wraps each {@link Runnable}
 * in a {@link DelegatingRequestContextRunnable} with {@code inheritable=true}.
 *
 * @see org.springframework.web.context.request.RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
 * @since 1.0
 */
public class InheritableDelegatingRequestContextExecutor
        extends DelegatingRequestContextExecutor
        implements Executor {

    /**
     * Creates a new {@link InheritableDelegatingRequestContextExecutor} that uses the specified
     * {@link RequestAttributes}.
     *
     * @param delegateExecutor  the {@link Executor} to delegate to. Cannot be null.
     * @param requestAttributes the {@link RequestAttributes} to use for each
     *                          {@link DelegatingRequestContextRunnable} or null to default to the current
     *                          {@link RequestAttributes}
     */
    public InheritableDelegatingRequestContextExecutor(Executor delegateExecutor, RequestAttributes requestAttributes) {
        super(delegateExecutor, requestAttributes, true);
    }

    /**
     * Creates a new {@link InheritableDelegatingRequestContextExecutor} that uses
     * the current {@link RequestAttributes} from
     * the {@link org.springframework.web.context.request.RequestContextHolder}
     * at the time the task is submitted.
     *
     * @param delegate the {@link Executor} to delegate to. Cannot be null.
     */
    public InheritableDelegatingRequestContextExecutor(Executor delegate) {
        this(delegate, null);
    }

}
