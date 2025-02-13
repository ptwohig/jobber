package com.patricktwohig.jobber.ai;

import com.patricktwohig.jobber.model.JobDescriptionAnalysis;
import com.patricktwohig.jobber.model.Resume;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

@SystemMessage(
        )
public interface JobDescriptionAnalyst {

    @SystemMessage(
            "You are an analyst researching job descriptions for job seekers to provide useful information to " +
                    "jobseekers. Your job is to determine how well the jobseeker fits the description and the " +
                    "company as well as give advice on the job description. You use sites like GlassDoor and " +
                    "Salary.com to rate employers and determine industry averages. In all cases, you must extract " +
                    "all information into the JSON format requested such that it may be fed into downstream " +
                    "processes. Please review the job description with the following url as well as the job " +
                    "seeker's resume in order to create a personalized report for the jobseeker based on their " +
                    "resume. Rank all scores on a scale of one to five. When determining quality score, base the " +
                    "determination on how well the job seeker fits to that role. When determining salary score, " +
                    "determine based on the precision of the salary range along with how well the advertised salary " +
                    "meets industry averages. In all cases, factor the job description and duties against industry " +
                    "norms and averages. In the personal recommendation, indicate how well the job seeker fits " +
                    "the description and company."
    )
    @UserMessage(
            """
                    Job Description - {{jobDescriptionUrl}}
                    Job Seeker ResumeCommands - {{jobSeekerResume}}
                    Company Website URL - {{companyWebsiteUrl}}
            """
    )
    JobDescriptionAnalysis analyzePublicJobDescriptionUrl(
            @V("jobSeekerResume") Resume resume,
            @V("companyWebsiteUrl") String companyWebsiteUrl,
            @V("jobDescriptionUrl") String jobDescriptionUrl
    );

}
