package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("You are a data analyst whose job is to read resumes and organize them into structured data.")
public interface ResumeAnalyst {

    @SystemMessage("Read the following resume and convert into the desired format.")
    @UserMessage("Jobseeker's Resume - {{resumePlainText}}")
    Resume analyzePlainText(@V("resumePlainText") String resumePlainText);

}
