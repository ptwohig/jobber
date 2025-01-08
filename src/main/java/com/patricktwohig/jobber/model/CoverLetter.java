package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class CoverLetter {

    private Contact sender;

    private Employer employer;

    private Contact reciepient;

    private List<String> paragraphs;

    public Contact getSender() {
        return sender;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Contact getReciepient() {
        return reciepient;
    }

    public void setReciepient(Contact reciepient) {
        this.reciepient = reciepient;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CoverLetter{");
        sb.append("sender=").append(sender);
        sb.append(", employer=").append(employer);
        sb.append(", reciepient=").append(reciepient);
        sb.append(", paragraphs=").append(paragraphs);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverLetter that = (CoverLetter) o;
        return Objects.equals(getSender(), that.getSender()) && Objects.equals(getEmployer(), that.getEmployer()) && Objects.equals(getReciepient(), that.getReciepient()) && Objects.equals(getParagraphs(), that.getParagraphs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getEmployer(), getReciepient(), getParagraphs());
    }

}
