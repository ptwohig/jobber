package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class JobDescriptionSummary {

    private String title;

    private String company;

    private String location;

    private String summary;

    private List<String> skills;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobDescriptionSummary that = (JobDescriptionSummary) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getCompany(), that.getCompany()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getSummary(), that.getSummary()) && Objects.equals(getSkills(), that.getSkills());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getCompany(), getLocation(), getSummary(), getSkills());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobDescriptionSummary{");
        sb.append("title='").append(title).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", skills=").append(skills);
        sb.append('}');
        return sb.toString();
    }

}
