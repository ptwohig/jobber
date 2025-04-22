package com.patricktwohig.jobber.model;

import java.util.Objects;

public class InteractiveResumeResponse {

    private Resume resume;

    private String remarks;

    private String summary;

    private String fileName;

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InteractiveResumeResponse that = (InteractiveResumeResponse) o;
        return Objects.equals(resume, that.resume) && Objects.equals(remarks, that.remarks) && Objects.equals(summary, that.summary) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resume, remarks, summary, fileName);
    }

    @Override
    public String toString() {
        return "InteractiveResumeResponse{" +
                "resume=" + resume +
                ", remarks='" + remarks + '\'' +
                ", summary='" + summary + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
