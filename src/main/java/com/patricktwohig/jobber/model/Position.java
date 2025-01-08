package com.patricktwohig.jobber.model;

import java.util.List;
import java.util.Objects;

public class Position {

    private String title;

    private String company;

    private String location;

    private PositionType positionType;

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

    public List<String> getAccomplishmentStatements() {
        return accomplishmentStatements;
    }

    public void setAccomplishmentStatements(List<String> accomplishmentStatements) {
        this.accomplishmentStatements = accomplishmentStatements;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("title='").append(title).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", positionType=").append(positionType);
        sb.append(", accomplishmentStatements=").append(accomplishmentStatements);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(getTitle(), position.getTitle()) && Objects.equals(getCompany(), position.getCompany()) && Objects.equals(getLocation(), position.getLocation()) && positionType == position.positionType && Objects.equals(getAccomplishmentStatements(), position.getAccomplishmentStatements());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getCompany(), getLocation(), positionType, getAccomplishmentStatements());
    }

}
