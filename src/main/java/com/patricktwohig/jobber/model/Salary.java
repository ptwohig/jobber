package com.patricktwohig.jobber.model;

import java.util.Objects;

public class Salary {

    private SalaryRange hourly;

    private SalaryRange annual;

    public SalaryRange getHourly() {
        return hourly;
    }

    public void setHourly(SalaryRange hourly) {
        this.hourly = hourly;
    }

    public SalaryRange getAnnual() {
        return annual;
    }

    public void setAnnual(SalaryRange annual) {
        this.annual = annual;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Salary{");
        sb.append("hourly=").append(hourly);
        sb.append(", annual=").append(annual);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salary = (Salary) o;
        return Objects.equals(getHourly(), salary.getHourly()) && Objects.equals(getAnnual(), salary.getAnnual());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHourly(), getAnnual());
    }

}
