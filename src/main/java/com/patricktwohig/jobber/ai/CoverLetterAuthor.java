package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.InteractiveCoverLetterResponse;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CoverLetterAuthor {

    @SystemMessage(
            """
            You author cover letters for jobseekers based on information provided. You must customize each cover \
            letter to the job description using the base cover letter. Write a cover letter for the supplied job \
            description. Use the supplied cover letter and resume as the basis. Highlight key accomplishments from the \
            resume and incoporate into the cover letter. Follow a three paragraph format. The first paragraph opens \
            with a brief introduction expressing desire and interest in the job, as well as indicates the jobseeker \
            understands the company's mission statement and core values. The second paragraph is a deeper in-depth \
            description of the candidate's qualifications and how they most relate to the job. When authoring the \
            second paragraph, use key facts from the job seeker's resume. Finally, end the letter with a brief call \
            to action requesting and interview as well as expressing gratitude for the consideration.
            """
    )
    @UserMessage(
            """
            Job Description - {{jobDescription}}
            Job Seeker's Resume - {{jobSeekersResume}}
            Base Cover Letter - {{baseCoverLetter}}
            """
    )
    CoverLetter tuneCoverLetterForResumeAndJobDescription(
            @V("baseCoverLetter") CoverLetter baseCoverLetter,
            @V("jobSeekersResume") Resume jobseekersResume,
            @V("jobDescription") String jobDescription
    );

    @SystemMessage(
            """
            You author cover letters for jobseekers based on information provided. The jobseeker will describe the \
            cover letter and what they want to modify. Adjust it according to the jobseeker's comments accordingly \
            using both the supplied cover as a base.
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
