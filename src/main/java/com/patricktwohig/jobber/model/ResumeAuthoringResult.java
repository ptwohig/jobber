package com.patricktwohig.jobber.model;

import java.util.Objects;

public class ResumeAuthoringResult {

    private float score;

    private String remarks;

    private String summary;

    private Resume result;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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

    public Resume getResult() {
        return result;
    }

    public void setResult(Resume result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResumeAuthoringResult that = (ResumeAuthoringResult) o;
        return Float.compare(getScore(), that.getScore()) == 0 && Objects.equals(getRemarks(), that.getRemarks()) && Objects.equals(getSummary(), that.getSummary()) && Objects.equals(getResult(), that.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScore(), getRemarks(), getSummary(), getResult());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResumeAuthoringResult{");
        sb.append("score=").append(score);
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

}
