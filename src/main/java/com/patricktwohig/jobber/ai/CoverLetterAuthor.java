package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CoverLetterAuthor {

    @SystemMessage(
            """
                    You author cover letters for jobseekers based on information provided. You must customize each \
                    cover letter to the job description using the base cover letter. Write a cover letter for the \
                    supplied job description at the supplied URL. Use the supplied cover letter as a base for \
                    the generated resume. Follow a three paragraph format. The first paragraph opens with a \
                    brief introduction expressing desire and interest in the job, as well as indicates \
                    the jobseeker understands the company's mission statement and core values. The second paragraph \
                    is a deeper in-depth description of the candidate's qualifications and how they most relate to \
                    the job. When authoring the second paragraph, use key facts from the job seeker's resume. \
                    Finally, end the letter with a brief call to action requesting and interview as well as \
                    expressing gratitude for the consideration.
            """
    )
    @UserMessage(
            """
                    Job Description - {{jobDescription}}
                    Job Seeker's ResumeCommands - {{jobSeekersResume}}
                    Base Cover Letter - {{baseCoverLetter}}
            """
    )
    CoverLetter tuneCoverLetterForResumeAndJobDescription(
            @V("jobSeekersResume") Resume jobseekersResume,
            @V("baseCoverLetter") CoverLetter baseCoverLetter,
            @V("jobDescription") String jobDescription
    );

    @SystemMessage(
            """
                    You author cover letters for jobseekers based on information provided. The jobseeker will describe 
                    the cover letter and what they want to see. Adjust it according to the jobseeker's comments. 
                    Keep as much of hte original document as possible and adjust only the language which is already 
                    in the document. Do not remove sections.
            """
    )
    @UserMessage(
            """
                    Base Cover Letter - {{baseCoverLetter}}
                    Jobseeker's Comments - {{jobSeekersComments}}
            """
    )
    Resume tuneResumeBasedOnJobSeekersDescription(
            @V("baseCoverLetter") Resume baseCoverLetter,
            @V("jobSeekersComments") String jobSeekersComments
    );

}
