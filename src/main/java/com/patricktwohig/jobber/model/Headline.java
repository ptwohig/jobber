package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Headline {

    private String title;

    private String summary;

    private List<String> skills;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("Headline{");
        sb.append("title='").append(title).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", skills=").append(skills);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Headline headline = (Headline) o;
        return Objects.equals(getTitle(), headline.getTitle()) && Objects.equals(getSummary(), headline.getSummary()) && Objects.equals(getSkills(), headline.getSkills());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSummary(), getSkills());
    }

}
