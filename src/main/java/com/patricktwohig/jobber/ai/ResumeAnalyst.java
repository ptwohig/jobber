package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAnalyst {

    @SystemMessage(
            """
            You are a data analyst tasked with organizing resumes into structured data. Extract key details from the
            resume and format them accordingly. For graduation dates, include only the year; the month and day are not
            required.
            """
    )
    @UserMessage("Jobseeker's ResumeCommands - {{resumePlainText}}")
    Resume analyzePlainText(@V("resumePlainText") String resumePlainText);

}
