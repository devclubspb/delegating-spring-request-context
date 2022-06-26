package ru.spb.devclub.spring.web.context.request.task;

import org.springframework.core.task.TaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextExecutor;

/**
 * An {@link TaskExecutor} which wraps each {@link Runnable} in a
 * {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}.
 *
 * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/task/DelegatingSecurityContextTaskExecutor.html">
 * DelegatingSecurityContextTaskExecutor
 * </a>
 * @since 1.0
 */
public class DelegatingRequestContextTaskExecutor extends DelegatingRequestContextExecutor implements TaskExecutor {

    /**
     * {@inheritDoc}
     */
    protected DelegatingRequestContextTaskExecutor(
            TaskExecutor delegateTaskExecutor,
            RequestAttributes requestAttributes,
            boolean inheritable) {
        super(delegateTaskExecutor, requestAttributes, inheritable);
    }

    /**
     * Creates a new {@link DelegatingRequestContextTaskExecutor} that uses the specified
     * {@link RequestAttributes}.
     *
     * @param delegateTaskExecutor the {@link TaskExecutor} to delegate to. Cannot be
     *                             null.
     * @param requestAttributes    the {@link RequestAttributes} to use for each
     *                             {@link ru.spb.devclub.spring.web.context.request.concurrent.DelegatingRequestContextRunnable}
     */
    public DelegatingRequestContextTaskExecutor(
            TaskExecutor delegateTaskExecutor,
            RequestAttributes requestAttributes) {
        this(delegateTaskExecutor, requestAttributes, false);
    }

    /**
     * Creates a new {@link DelegatingRequestContextTaskExecutor} that uses the current
     * {@link RequestAttributes} from the {@link org.springframework.web.context.request.RequestContextHolder}.
     *
     * @param delegate the {@link TaskExecutor} to delegate to. Cannot be null.
     */
    public DelegatingRequestContextTaskExecutor(TaskExecutor delegate) {
        this(delegate, null);
    }

}
