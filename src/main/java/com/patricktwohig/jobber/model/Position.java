package com.patricktwohig.jobber.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Position {

    private String title;

    private String company;

    private String location;

    private PositionType positionType;

    private LocalDate startDate;

    private LocalDate endDate;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Position{" +
                "title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", positionType=" + positionType +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", accomplishmentStatements=" + accomplishmentStatements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(title, position.title) && Objects.equals(company, position.company) && Objects.equals(location, position.location) && positionType == position.positionType && Objects.equals(startDate, position.startDate) && Objects.equals(endDate, position.endDate) && Objects.equals(accomplishmentStatements, position.accomplishmentStatements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, company, location, positionType, startDate, endDate, accomplishmentStatements);
    }
}
