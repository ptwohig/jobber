package com.patricktwohig.jobber.model;

import java.util.Objects;

public class UndoOperation {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UndoOperation that = (UndoOperation) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(count);
    }

    @Override
    public String toString() {
        return "UndoOperation{" +
                "count=" + count +
                '}';
    }

}
