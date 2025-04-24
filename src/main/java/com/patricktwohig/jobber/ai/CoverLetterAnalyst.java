package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.GeneralRemarks;
import com.patricktwohig.jobber.model.JobDescriptionSummary;
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

    @SystemMessage(
            """
            You analyze job descriptions and cover letters. Provide feedback on how well the cover letter matches the 
            job description, including a relevance score (0-100), remarks, and a brief summary.            
            """
    )
    @UserMessage(
            """
            Cover Letter: {{coverLetter}}
            """
    )
    GeneralRemarks analyzeCoverLetter(@V("coverLetter") CoverLetter coverLetter);

    @SystemMessage(
            """
            You analyze job descriptions and cover letters. Provide feedback on how well the cover letter matches the 
            job description, including a relevance score (0-100), remarks, and a brief summary.            
            """
    )
    @UserMessage(
            """
            Cover Letter: {{coverLetter}}
            Job Description: {{jobDescription}}
            """
    )
    GeneralRemarks analyzeCoverLetter(
            @V("coverLetter") CoverLetter coverLetter,
            @V("jobDescription") JobDescriptionSummary jobDescription);

}
