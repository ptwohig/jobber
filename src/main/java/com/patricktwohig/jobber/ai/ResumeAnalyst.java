package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAnalyst {

    @SystemMessage(
            "You are a data analyst whose job is to read resumes and organize them into structured data."  +
            "Read the following resume and convert into the desired format. When determining graduation date " +
            "it is acceptable to list only the year. The month and date are not crucial"
    )
    @UserMessage("Jobseeker's ResumeCommands - {{resumePlainText}}")
    Resume analyzePlainText(@V("resumePlainText") String resumePlainText);

}
