package com.patricktwohig.jobber.model;

import java.util.Objects;

public class TaskResult {

    private Task task;

    private String remarks;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskResult that = (TaskResult) o;
        return getTask() == that.getTask() && Objects.equals(getRemarks(), that.getRemarks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTask(), getRemarks());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskResult{");
        sb.append("task=").append(task);
        sb.append(", response='").append(remarks).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
