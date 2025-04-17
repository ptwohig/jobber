package com.patricktwohig.jobber.model;

import java.util.Objects;

public class CoverLetterAuthoringResult {

    private float score;

    private String remarks;

    private String summary;

    private CoverLetter result;

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

    public CoverLetter getResult() {
        return result;
    }

    public void setResult(CoverLetter result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoverLetterAuthoringResult that = (CoverLetterAuthoringResult) o;
        return Float.compare(getScore(), that.getScore()) == 0 && Objects.equals(getRemarks(), that.getRemarks()) && Objects.equals(getSummary(), that.getSummary()) && Objects.equals(getResult(), that.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScore(), getRemarks(), getSummary(), getResult());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CoverLetterAuthoringResult{");
        sb.append("score=").append(score);
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

}
