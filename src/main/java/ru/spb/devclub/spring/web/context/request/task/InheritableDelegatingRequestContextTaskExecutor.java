package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.TaskExecutor;
import org.springframework.web.context.request.RequestAttributes;

/**
 * An {@link TaskExecutor} which wraps each {@link Runnable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
 * with {@code inheritable=true}.
 *
 * @see org.springframework.web.context.request.RequestContextHolder#setRequestAttributes(RequestAttributes, boolean)
 * @since 1.0
 */
public class InheritableDelegatingRequestContextTaskExecutor
        extends DelegatingRequestContextTaskExecutor
        implements TaskExecutor {

    /**
     * Creates a new {@link InheritableDelegatingRequestContextTaskExecutor} that uses the specified
     * {@link RequestAttributes}.
     *
     * @param delegateTaskExecutor the {@link TaskExecutor} to delegate to. Cannot be
     *                             null.
     * @param requestAttributes    the {@link RequestAttributes} to use for each
     *                             {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
     */
    public InheritableDelegatingRequestContextTaskExecutor(
            TaskExecutor delegateTaskExecutor,
            RequestAttributes requestAttributes) {
        super(delegateTaskExecutor, requestAttributes, true);
    }

    /**
     * Creates a new {@link InheritableDelegatingRequestContextTaskExecutor} that uses the current
     * {@link RequestAttributes} from the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link TaskExecutor} to delegate to. Cannot be null.
     */
    public InheritableDelegatingRequestContextTaskExecutor(TaskExecutor delegate) {
        this(delegate, null);
    }

}
