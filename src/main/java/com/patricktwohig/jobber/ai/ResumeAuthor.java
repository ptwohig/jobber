package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage(
        "You author resumes for jobseekers based on information provided. You must customize each cover letter " +
        "to the job description and the using the base cover letter."
)
public interface ResumeAuthor {

    @UserMessage(
            "Write a resume for the supplied job description at the supplied URL. Use the supplied resume as a " +
                    "base for the generated resume. Ensure that the resume's title matches that of the job " +
                    "description as well as ensure that the resume contains specific keywords mentioned in job " +
                    "description." +
                    "\n" +
                    "Base ResumeCommands - {{baseResume}}\n" +
                    "Job Description URL - {{jobDescriptionUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("baseResume") String baseResume,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

    @UserMessage(
            "Write a resume for the supplied job description at the supplied URL. Use the supplied resume as a " +
                "base for the generated resume. Ensure that the resume's title matches that of the job description " +
                "as well as ensure that the resume contains specific keywords mentioned in job description." +
                "\n" +
                "Base ResumeCommands - {{baseResume}}\n" +
                "Job Description URL - {{jobDescriptionUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("baseResume") Resume baseResume,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

}
