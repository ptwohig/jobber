package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("You are a data analyst whose job is to read cover letters and organize them into structured data.")
public interface CoverLetterAnalyst {

    @SystemMessage(
            "Read the following resume and convert into the desired format. Organize into sender, recipient " +
            "(if available), and paragraphs.")
    @UserMessage("Jobseeker's Cover Letter - {{coverLetterPlainText}}")
    CoverLetter analyzePlainText(@V("coverLetterPlainText") String resumePlainText);

}
