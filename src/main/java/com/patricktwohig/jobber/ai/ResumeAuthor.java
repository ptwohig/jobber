package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.InteractiveResumeResponse;
import com.patricktwohig.jobber.model.JobDescriptionSummary;
import com.patricktwohig.jobber.model.Resume;
import com.patricktwohig.jobber.model.ResumeAuthoringResult;
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
            positions of titles, dates, and locations. Remove skills not related to the job description. Include your
            remarks, summary of edits, and a score from 0 to 100. The score should be based on the relevance of the
            resume to the job description.
            """
    )
    @UserMessage(
            """
            Original Resume: {{baseResume}}
            Job Description: {{jobDescription}}
            """
    )
    ResumeAuthoringResult tuneResumeForPublicJobDescription(
            @V("baseResume") Resume baseResume,
            @V("jobDescription") String jobDescription
    );

    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. The jobseeker will describe the
            resume and what they want to see. Adjust it according to the jobseeker's comments. Provide a brief filename
            without an extension.
            """
    )
    @UserMessage(
            """
            Jobseeker's Comments: {{jobSeekersComments}}
            """
    )
    InteractiveResumeResponse tuneResumeBasedOnJobSeekersComments(
            @V("jobSeekersComments") String jobSeekersComments
    );

    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. The jobseeker will describe the \
            resume and what they want to see. Adjust it according to the jobseeker's comments. Preserve all \
            positions, including their titles, dates, and locations, and ensure no sections are removed. Provide \
            a brief filename without an extension.
            """
    )
    @UserMessage(
            """
            Base Resume: {{baseResume}}
            Jobseeker's Comments: {{jobSeekersComments}}
            """
    )
    InteractiveResumeResponse tuneResumeBasedOnJobSeekersComments(
            @V("baseResume") Resume baseResume,
            @V("jobSeekersComments") String jobSeekersComments
    );

    @SystemMessage(
            """
            You author resumes for jobseekers based on information provided. The jobseeker will describe the \
            resume and what they want to see. Adjust it according to the jobseeker's comments. Preserve all \
            positions, including their titles, dates, and locations, and ensure no sections are removed. Provide \
            a brief filename without an extension.
            """
    )
    @UserMessage(
            """
            Base Resume: {{baseResume}}
            Job Description Summary: {{jobDescriptionSummary}}
            Jobseeker's Comments: {{jobSeekersComments}}
            """
    )
    InteractiveResumeResponse tuneResumeBasedOnJobSeekersComments(
            @V("baseResume") Resume baseResume,
            @V("jobDescriptionSummary") JobDescriptionSummary jobDescriptionSummary,
            @V("jobSeekersComments") String jobSeekersComments
    );

}
