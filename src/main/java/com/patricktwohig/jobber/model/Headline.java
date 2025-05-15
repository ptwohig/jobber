package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Headline {

    private String title;

    private String summary;

    private List<String> skills;

    private List<String> leadershipHighlights;

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

    public List<String> getLeadershipHighlights() {
        return leadershipHighlights;
    }

    public void setLeadershipHighlights(List<String> leadershipHighlights) {
        this.leadershipHighlights = leadershipHighlights;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Headline headline = (Headline) object;
        return Objects.equals(getTitle(), headline.getTitle()) && Objects.equals(getSummary(), headline.getSummary()) && Objects.equals(getSkills(), headline.getSkills()) && Objects.equals(getLeadershipHighlights(), headline.getLeadershipHighlights());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSummary(), getSkills(), getLeadershipHighlights());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Headline{");
        sb.append("title='").append(title).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", skills=").append(skills);
        sb.append(", leadershipHighlights=").append(leadershipHighlights);
        sb.append('}');
        return sb.toString();
    }
    
}
