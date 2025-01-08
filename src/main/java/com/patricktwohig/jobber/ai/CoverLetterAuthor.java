package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("You author cover letters for jobseekers based on information provided.")
public interface CoverLetterAuthor {

    @UserMessage(
            "Write a cover letter for the supplied job description at the supplied URL. Use the supplied cover letter " +
            "as a base for the generated resume. Ensure that the cover letter contains specific keywords from the job " +
            "description. If possible, try to find the hiring manager's name or leave blank if it can't be found." +
            "\n" +
            "Base Cover Letter - {{baseCoverLetter}}\n" +
            "Job Description URL - {{jobDescriptionUrl}}\n"
    )
    Resume tuneResumeForPublicJobDescriptionUrl(
            @V("baseCoverLetter") Resume baseCoverLetter,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

}
