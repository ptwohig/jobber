package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.JobDescription;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage("You are an analyst researching job descriptions for job seekers to provide useful information to the jobseekers.")
public interface JobDescriptionAnalyst {

    @UserMessage(
            "Please review the job description with the following url as well as the job seeker's resume in order to " +
            "create a personalized report for the jobseeker based on their resume. Rank all scores on a scale of one " +
            "to five. When determining quality score, base the determination on how well the job seeker fits to that " +
            "role. When determining salary score, determine based on the precision of the salary range along with " +
            "how well the advertised salary meets industry averages. In all cases, factor the job description and " +
            "duties against industry norms and averages." +
            "\n" +
            "Job Description - {{jobDescriptionUrl}}\n" +
            "Job Seeker Resume - {{jobSeekerResume}}\n"
    )
    JobDescription analyzePublicJobDescriptionUrl(
            @V("jobSeekerResume") Resume resume,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

}
