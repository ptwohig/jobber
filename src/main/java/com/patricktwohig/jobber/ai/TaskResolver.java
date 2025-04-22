package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Task;
import com.patricktwohig.jobber.model.TaskResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * The task resolver is responsible for determining the task that the user is requesting.
 */
public interface TaskResolver {

    /**
     * Invokes {@link #resolve(String, String)} with detailed task descriptions.
     *
     * @param prompt the prompt
     */
    default TaskResult resolve(@V("prompt") String prompt) {
        final var tasks = Stream.of(Task.values())
                .map(t -> format("%s: %s", t.name(), t.getDescription()))
                .collect(Collectors.joining("\n"));
        return resolve(prompt, tasks);
    }

    @SystemMessage(
            """
            You are tasked with identifying the user's request. Analyze the provided prompt and select the most
            appropriate task from the list below. You will not be performing the task, just identifying it.

            Task list:
            {{tasks}}
            """
    )
    @UserMessage("{{prompt}}")
    TaskResult resolve(@V("prompt") String prompt, @V("tasks") String tasks);

}
