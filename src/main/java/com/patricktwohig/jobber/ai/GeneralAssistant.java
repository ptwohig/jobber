package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.GeneralFeedback;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface GeneralAssistant {

    @SystemMessage(
            """
            You are a helpful assistant to jobseekers. Your task is to provide feedback on the text provided to you. 
            Ensure that your feedback is constructive and relevant to the content. If necessary, explain your role to 
            the jobseeker and clarify.
            """
    )
    @UserMessage("{{prompt}}")
    GeneralFeedback provideGeneralFeedback(@V("prompt") String prompt);

}
