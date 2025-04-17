package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CoverLetterAnalyst {

    @SystemMessage(
        "You are a data analyst tasked with analyzing cover letters and structuring them into organized data. " +
        "Extract and format the information into sender, recipient (if available), and paragraphs."
    )
    @UserMessage("Jobseeker's Cover Letter - {{coverLetterPlainText}}")
    CoverLetter analyzePlainText(@V("coverLetterPlainText") String resumePlainText);

}
