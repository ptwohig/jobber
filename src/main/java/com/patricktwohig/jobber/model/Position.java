package com.patricktwohig.jobber.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Position {

    private String title;

    private String company;

    private String location;

    private PositionType positionType;

    private String startDate;

    private String endDate;

    private Link website;

    private List<String> accomplishmentStatements;

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

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public List<String> getAccomplishmentStatements() {
        return accomplishmentStatements;
    }

    public void setAccomplishmentStatements(List<String> accomplishmentStatements) {
        this.accomplishmentStatements = accomplishmentStatements;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Link getWebsite() {
        return website;
    }

    public void setWebsite(Link website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(getTitle(), position.getTitle()) && Objects.equals(getCompany(), position.getCompany()) && Objects.equals(getLocation(), position.getLocation()) && getPositionType() == position.getPositionType() && Objects.equals(getStartDate(), position.getStartDate()) && Objects.equals(getEndDate(), position.getEndDate()) && Objects.equals(getWebsite(), position.getWebsite()) && Objects.equals(getAccomplishmentStatements(), position.getAccomplishmentStatements());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getCompany(), getLocation(), getPositionType(), getStartDate(), getEndDate(), getWebsite(), getAccomplishmentStatements());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("title='").append(title).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", positionType=").append(positionType);
        sb.append(", startDate='").append(startDate).append('\'');
        sb.append(", endDate='").append(endDate).append('\'');
        sb.append(", website=").append(website);
        sb.append(", accomplishmentStatements=").append(accomplishmentStatements);
        sb.append('}');
        return sb.toString();
    }

}
