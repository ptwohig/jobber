package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.TaskResult;

public class DefaultTaskResolver implements TaskResolver {

    private final TaskResolver delegate;

    public DefaultTaskResolver(final TaskResolver delegate) {
        this.delegate = delegate;
    }

    public TaskResult resolve(final String prompt, final String tasks, final String approval) {
        return delegate.resolve(prompt, tasks, approval);
    }

}
