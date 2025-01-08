package com.patricktwohig.jobber.model;

import java.util.Objects;

public class SalaryRange {

    private int minimum;

    private int maximum;

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalaryRange that = (SalaryRange) o;
        return getMinimum() == that.getMinimum() && getMaximum() == that.getMaximum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinimum(), getMaximum());
    }

}
