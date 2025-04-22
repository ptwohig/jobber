package com.patricktwohig.jobber.model;

import java.util.Objects;

public class GeneralRemarks {

    private Double score;

    private String remarks;

    private String summary;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeneralRemarks that = (GeneralRemarks) o;
        return Objects.equals(getScore(), that.getScore()) && Objects.equals(getRemarks(), that.getRemarks()) && Objects.equals(getSummary(), that.getSummary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScore(), getRemarks(), getSummary());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GeneralRemarks{");
        sb.append("score=").append(score);
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
