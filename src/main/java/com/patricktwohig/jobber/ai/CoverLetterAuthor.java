package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.CoverLetter;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage(
        "You author cover letters for jobseekers based on information provided. You must customize each cover letter " +
        "to the job description using the base cover letter.")
public interface CoverLetterAuthor {

    @SystemMessage(
            "Write a cover letter for the supplied job description at the supplied URL. Use the supplied cover " +
                    "letter as a base for the generated resume. Follow a three paragraph format. The first paragraph " +
                    "opens with a brief introduction expressing desire and interest in the job, as well as indicates " +
                    "the jobseeker understands the company's mission statement and core values. The second paragraph " +
                    "is a deeper in-depth description of the candidate's qualifications and how they most relate to " +
                    "the job. When authoring the second paragraph, use key facts from the job seeker's resume. " +
                    "Finally, end the letter with a brief call to action requesting and interview as well as " +
                    "expressing gratitude for the consideration."
    )
    @UserMessage(
            "Job Seeker's ResumeCommands - {{jobSeekersResume}}\n" +
                    "Base Cover Letter - {{baseCoverLetter}}\n" +
                    "Job Description URL - {{jobDescriptionUrl}}\n" +
                    "Company Website URL - {{companyWebsiteUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("jobSeekersResume") String jobseekersResume,
            @V("baseCoverLetter") String baseCoverLetter,
            @V("jobDescriptionUrl") String jobDescriptionUrl,
            @V("companyWebsiteUrl") String companyWebsiteUrl
    );

    @SystemMessage(
            "Write a cover letter for the supplied job description at the supplied URL. Use the supplied cover " +
                    "letter as a base for the generated resume. Follow a three paragraph format. The first paragraph " +
                    "opens with a brief introduction expressing desire and interest in the job, as well as indicates " +
                    "the jobseeker understands the company's mission statement and core values. The second paragraph " +
                    "is a deeper in-depth description of the candidate's qualifications and how they most relate to " +
                    "the job. When authoring the second paragraph, use key facts from the job seeker's resume. " +
                    "Finally, end the letter with a brief call to action requesting and interview as well as " +
                    "expressing gratitude for the consideration."
    )
    @UserMessage(
            "Job Seeker's ResumeCommands - {{jobSeekersResume}}\n" +
                "Base Cover Letter - {{baseCoverLetter}}\n" +
                "Job Description URL - {{jobDescriptionUrl}}\n" +
                "Company Website URL - {{companyWebsiteUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("jobSeekersResume") Resume jobseekersResume,
            @V("baseCoverLetter") CoverLetter baseCoverLetter,
            @V("jobDescriptionUrl") String jobDescriptionUrl,
            @V("companyWebsiteUrl") String companyWebsiteUrl
    );

}
