package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.InteractiveResumeResponse;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAuthor {


    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. You must customize each resume \
            to the job description using the supplied resume as the base resume. Write a resume for the \
            supplied job description at the supplied URL. Use the supplied resume as a base for the \
            generated resume. Ensure that the resume's title matches that of the job description as well \
            as ensure that the resume contains specific keywords mentioned in job description. Keep all the \
            time span of the resume the same to reflect the total years of the jobseeker's experience, \
            preferring to leave sections in tact that are less relevant. Do not omit jobs.
            """
    )
    @UserMessage(
            """
            Base Resume - {{baseResume}}
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
            resume and what they want to see. Adjust it according to the jobseeker's comments. Keep as much \
            of the original document as possible and adjust only the language which is already in the document. \
            Also provide your remarks and responses to any questions asked by the jobseeker.
            """
    )
    InteractiveResumeResponse tuneResumeBasedOnJobSeekersComments(
            @V("baseResume") Resume baseResume,
            @V("jobSeekersComments") String jobSeekersComments
    );

}
