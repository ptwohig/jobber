package com.patricktwohig.jobber.model;

import java.util.Objects;

public class InteractiveResumeResponse {

    private Resume resume;

    private String remarks;

    private String summary;

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InteractiveResumeResponse that = (InteractiveResumeResponse) o;
        return Objects.equals(getResume(), that.getResume()) && Objects.equals(getRemarks(), that.getRemarks()) && Objects.equals(getSummary(), that.getSummary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResume(), getRemarks(), getSummary());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InteractiveResumeResponse{");
        sb.append("resume=").append(resume);
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
