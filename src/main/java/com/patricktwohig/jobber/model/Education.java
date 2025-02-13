package com.patricktwohig.jobber.model;

import java.time.LocalDate;
import java.util.Objects;

public class Education {

    private String schoolName;

    private String degreeEarned;

    private String major;

    private String graduationDate;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDegreeEarned() {
        return degreeEarned;
    }

    public void setDegreeEarned(String degreeEarned) {
        this.degreeEarned = degreeEarned;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Education{");
        sb.append("schoolName='").append(schoolName).append('\'');
        sb.append(", degreeEarned='").append(degreeEarned).append('\'');
        sb.append(", major='").append(major).append('\'');
        sb.append(", graduationDate=").append(graduationDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Education education = (Education) o;
        return Objects.equals(getSchoolName(), education.getSchoolName()) && Objects.equals(getDegreeEarned(), education.getDegreeEarned()) && Objects.equals(getMajor(), education.getMajor()) && Objects.equals(getGraduationDate(), education.getGraduationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchoolName(), getDegreeEarned(), getMajor(), getGraduationDate());
    }

}
