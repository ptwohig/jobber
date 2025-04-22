package com.patricktwohig.jobber.model;

/**
 * The task that the user is requesting.
 */
public enum Task {

    /**
     * No specific operation was requested.
     */
    NO_TASK_REQUESTED("No task requested."),

    /**
     * The user is requesting to exit the application.
     */
    QUIT("The prompt is requesting to quit the application."),

    /**
     * The user requests general analysis on the resume.
     */
    PROVIDE_RESUME_ANALYSIS("Give feedback on a resume based on the job description."),

    /**
     * The user requests general analysis on the resume.
     */
    PROVIDE_COVER_LETTER_ANALYSIS("Analyze the cover letter and compare to the job description."),

    /**
     * The user is requesting a tuning of their resume.
     */
    UPDATE_RESUME_WITH_COMMENTS("Update the resume based on the comments."),

    /**
     * The user is requesting a tuning of their resume.
     */
    UPDATE_COVER_LETTER_WITH_COMMENTS("Update the cover letter based on the comments."),;

    private final String description;

    /**
     * Describes the task.
     *
     * @param description the description of the task
     */
    Task(final String description) {
        this.description = description;
    }

    /**
     * Returns the description of the task.
     *
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

}
