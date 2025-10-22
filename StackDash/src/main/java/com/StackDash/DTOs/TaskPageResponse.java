package com.StackDash.DTOs;

import java.util.List;

public class TaskPageResponse {

    private List<TaskDto> tasks;
    private long totalElements;
    private int totalPages;

    public TaskPageResponse(List<TaskDto> tasks, long totalElements, int totalPages) {
        this.tasks = tasks;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
