package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.InteractiveResumeResponse;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAuthor {


    @SystemMessage(
            """
            You author resumes for jobseekers based on the provided information. Customize the resume to the job
            description using the supplied resume as a base. Match the resume's title to the job description and
            include specific keywords from it. Maintain the total years of experience and sort experience by end date,
            placing current roles first. Assume "present" for roles without an end date. Preserve the original
            positions of titles, dates, and locations.
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
