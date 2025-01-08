package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("You are data analyst whose job it is to read documents and convert them to structured data.")
public interface ResumeAnalyst {

    @UserMessage(
            "Parse the following text for a job applicant and convert it to a resume in structured form: " +
            "{{resumePlainText}}"
    )
    Resume analyzePlainText(@V("resumePlainText") String resumePlainText);

}
