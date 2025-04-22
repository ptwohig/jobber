package com.patricktwohig.jobber.model;

import java.util.Objects;

public class TaskResult {

    private Task task;

    private String remarks;

    private Approval approval;

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

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval approval) {
        this.approval = approval;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskResult that = (TaskResult) o;
        return task == that.task && Objects.equals(remarks, that.remarks) && approval == that.approval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, remarks, approval);
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "task=" + task +
                ", remarks='" + remarks + '\'' +
                ", approval=" + approval +
                '}';
    }
    
}
