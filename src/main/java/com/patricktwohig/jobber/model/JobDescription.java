package com.patricktwohig.jobber.model;

import java.util.Objects;

public class JobDescription {

    private String title;

    private String description;

    private double qualityScore;

    private double salaryScore;

    private Salary advertised;

    private Salary industryAverage;

    private Employer employer;

    private Contact hiringManager;

    private String personalRecommendations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public double getSalaryScore() {
        return salaryScore;
    }

    public void setSalaryScore(double salaryScore) {
        this.salaryScore = salaryScore;
    }

    public Salary getAdvertised() {
        return advertised;
    }

    public void setAdvertised(Salary advertised) {
        this.advertised = advertised;
    }

    public Salary getIndustryAverage() {
        return industryAverage;
    }

    public void setIndustryAverage(Salary industryAverage) {
        this.industryAverage = industryAverage;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Contact getHiringManager() {
        return hiringManager;
    }

    public void setHiringManager(Contact hiringManager) {
        this.hiringManager = hiringManager;
    }

    public String getPersonalRecommendations() {
        return personalRecommendations;
    }

    public void setPersonalRecommendations(String personalRecommendations) {
        this.personalRecommendations = personalRecommendations;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobDescription{");
        sb.append("title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", qualityScore=").append(qualityScore);
        sb.append(", salaryScore=").append(salaryScore);
        sb.append(", advertised=").append(advertised);
        sb.append(", industryAverage=").append(industryAverage);
        sb.append(", employer=").append(employer);
        sb.append(", hiringManager=").append(hiringManager);
        sb.append(", personalRecommendations='").append(personalRecommendations).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDescription that = (JobDescription) o;
        return Double.compare(getQualityScore(), that.getQualityScore()) == 0 && Double.compare(getSalaryScore(), that.getSalaryScore()) == 0 && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getAdvertised(), that.getAdvertised()) && Objects.equals(getIndustryAverage(), that.getIndustryAverage()) && Objects.equals(getEmployer(), that.getEmployer()) && Objects.equals(getHiringManager(), that.getHiringManager()) && Objects.equals(getPersonalRecommendations(), that.getPersonalRecommendations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getQualityScore(), getSalaryScore(), getAdvertised(), getIndustryAverage(), getEmployer(), getHiringManager(), getPersonalRecommendations());
    }
}
