package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.GeneralRemarks;
import com.patricktwohig.jobber.model.JobDescriptionSummary;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface JobDescriptionAnalyst {

    @SystemMessage(
            """
            You summarize job descriptions. Provide a brief summary of the job description, highlighting key points and
            requirements.
            """
    )
    @UserMessage("Summarize the job description.")
    JobDescriptionSummary summarizeJobDescription();

    @SystemMessage(
            """
            You summarize job descriptions. Provide a brief summary of the job description, highlighting key points and
            requirements.
            """
    )
    @UserMessage(
            """
            Job Description: {{jobDescription}}
            """
    )
    JobDescriptionSummary summarizeJobDescription(
            @V("jobDescription") String jobDescription
    );

    @SystemMessage(
            """
            You analyse job descriptions and resumes. Your task is to analyze the job description and resume, providing
            a score from 0 to 100 based on their relevance. Include your remarks and a brief summary of the analysis.
            """
    )
    @UserMessage(
            """
            Job Description: {{jobDescription}}
            """
    )
    GeneralRemarks analyzeJobDescription(
            @V("jobDescription") String jobDescription
    );

}
