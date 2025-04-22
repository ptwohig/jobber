package com.patricktwohig.jobber.model;

import java.util.Objects;

public class InteractiveCoverLetterResponse {

    private String remarks;

    private CoverLetter coverLetter;

    private String fileName;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CoverLetter getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(CoverLetter coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InteractiveCoverLetterResponse that = (InteractiveCoverLetterResponse) o;
        return Objects.equals(remarks, that.remarks) && Objects.equals(coverLetter, that.coverLetter) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remarks, coverLetter, fileName);
    }

    @Override
    public String toString() {
        return "InteractiveCoverLetterResponse{" +
                "remarks='" + remarks + '\'' +
                ", coverLetter=" + coverLetter +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
