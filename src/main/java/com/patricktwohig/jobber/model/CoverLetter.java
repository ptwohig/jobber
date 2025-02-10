package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class CoverLetter {

    private Contact sender;

    private Employer employer;

    private Contact recipient;

    private List<String> bodyParagraphs;

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

    public Contact getRecipient() {
        return recipient;
    }

    public void setRecipient(Contact recipient) {
        this.recipient = recipient;
    }

    public List<String> getBodyParagraphs() {
        return bodyParagraphs;
    }

    public void setBodyParagraphs(List<String> bodyParagraphs) {
        this.bodyParagraphs = bodyParagraphs;
    }

    @Override
    public String toString() {
        return "CoverLetter{" +
                "sender=" + sender +
                ", employer=" + employer +
                ", reciepient=" + recipient +
                ", paragraphs=" + bodyParagraphs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverLetter that = (CoverLetter) o;
        return Objects.equals(sender, that.sender) && Objects.equals(employer, that.employer) && Objects.equals(recipient, that.recipient) && Objects.equals(bodyParagraphs, that.bodyParagraphs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, employer, recipient, bodyParagraphs);
    }

}
