package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.CoverLetterAuthoringResult;
import com.patricktwohig.jobber.model.InteractiveCoverLetterResponse;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CoverLetterAuthor {

    @SystemMessage(
            """
            You author cover letters for jobseekers based on provided information. Customize each cover letter to the
            job description using the base cover letter and resume. Highlight key accomplishments from the resume and
            incorporate them into the cover letter. Follow a three-paragraph format:
            1. Introduce the candidate, express interest in the job, and show understanding of the company's mission and values.
            2. Detail the candidate's qualifications and relate them to the job using key facts from the resume.
            3. Conclude with a call to action requesting an interview and express gratitude for the opportunity.
            Include your remarks, summary of edits, and a score from 0 to 100. The score should be based on the
            relevance of the resume and cover letter to the job description.
            """
    )
    @UserMessage(
            """
            Job Description: {{jobDescription}}
            Job Seeker's Resume: {{jobSeekersResume}}
            Base Cover Letter: {{baseCoverLetter}}
            """
    )
    CoverLetterAuthoringResult tuneCoverLetterForResumeAndJobDescription(
            @V("baseCoverLetter") CoverLetter baseCoverLetter,
            @V("jobSeekersResume") Resume jobseekersResume,
            @V("jobDescription") String jobDescription
    );

    @SystemMessage(
            """
            You author cover letters for jobseekers based on the provided information. Modify the cover letter
            according to the jobseeker's comments, using the supplied cover letter as a base.
            """
    )
    @UserMessage(
            """
            Base Cover Letter - {{baseCoverLetter}}
            Jobseeker's Comments - {{jobSeekersComments}}
            """
    )
    InteractiveCoverLetterResponse tuneCoverLetterBasedOnJobSeekersComments(
            @V("baseCoverLetter") CoverLetter baseCoverLetter,
            @V("jobSeekersComments") String jobSeekersComments
    );

}
