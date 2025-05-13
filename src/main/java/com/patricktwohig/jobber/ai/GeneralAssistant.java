package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.GeneralFeedback;
import com.patricktwohig.jobber.model.UndoOperation;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface GeneralAssistant {

    @SystemMessage(
            """
            You are a helpful assistant to jobseekers. Your task is to provide feedback on the text provided to you.
            Ensure that your feedback is constructive and relevant to the content. If necessary, explain your rationale
            to the jobseeker and clarify.
            """
    )
    @UserMessage("{{prompt}}")
    GeneralFeedback provideGeneralFeedback(@V("prompt") String prompt);

    @SystemMessage(
            """
            You are a helpful assistant to jobseekers. Determine the count of operations the jobseeker wishes to undo.
            Maximum Operations Availble: {{maxOperations}}
            """
    )
    @UserMessage("{{prompt}}")
    UndoOperation undoTheRequestedActions(
            @V("prompt") String prompt,
            @V("maxOperations") int maxOperations);

}
