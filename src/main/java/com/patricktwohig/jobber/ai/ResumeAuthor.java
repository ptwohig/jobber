package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResumeAuthor {

    @UserMessage(
            "You author resumes for jobseekers based on information provided. You must customize each resume " +
                    "to the job description using the supplied resume as the base resume. Write a resume for the " +
                    "supplied job description at the supplied URL. Use the supplied resume as a base for the " +
                    "generated resume. Ensure that the resume's title matches that of the job description as well " +
                    "as ensure that the resume contains specific keywords mentioned in job description. Keep all the " +
                    "time span of the resume the same to reflect the total years of the jobseeker's experience, " +
                    "preferring to leave sections in tact that are less relevant. Do not omit jobs."+
                    "\n" +
                    "Base ResumeCommands - {{baseResume}}\n" +
                    "Job Description - {{jobDescriptionUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("baseResume") Resume baseResume,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

}
