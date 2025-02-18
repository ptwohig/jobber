package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.InteractiveResumeResponse;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAuthor {


    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. You must customize the resume \
            to the job description using the supplied resume as the base resume. Ensure that the resume's title \
            matches that of the job description as well as ensure that the resume contains specific keywords mentioned \
            in job description. Keep all the time span of the resume the same to reflect the total years of the \
            jobseeker's experience. Match skills and experience to what is requested in the job description. Ensure \
            that experience is sorted by the end date, and assume present where no end date exists. Put present \
            experience first. Preserve the positions of titles, dates, and locations as they are in the original \
            resume.
            """
    )
    @UserMessage(
            """
            Original Resume - {{baseResume}}
            Job Description - {{jobDescription}}
            """
    )
    Resume tuneResumeForPublicJobDescription(
            @V("baseResume") Resume baseResume,
            @V("jobDescription") String jobDescription
    );

    @UserMessage(
            """
            Base Resume - {{baseResume}}
            Jobseeker's Comments - {{jobSeekersComments}}
            """
    )
    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. The jobseeker will describe the \
            resume and what they want to see. Adjust it according to the jobseeker's comments.
            """
    )
    InteractiveResumeResponse tuneResumeBasedOnJobSeekersComments(
            @V("baseResume") Resume baseResume,
            @V("jobSeekersComments") String jobSeekersComments
    );

}
