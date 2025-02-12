package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Resume {

    private Contact contact;

    private Headline headline;

    private List<Position> positions;

    private List<Education> educations;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResumeCommands{");
        sb.append("contact=").append(contact);
        sb.append(", headline=").append(headline);
        sb.append(", positions=").append(positions);
        sb.append(", educations=").append(educations);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(getContact(), resume.getContact()) && Objects.equals(getHeadline(), resume.getHeadline()) && Objects.equals(getPositions(), resume.getPositions()) && Objects.equals(getEducations(), resume.getEducations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContact(), getHeadline(), getPositions(), getEducations());
    }

}
