package com.StackDash.DTOs;

import com.StackDash.Entity.Task;
import com.StackDash.Entity.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Date;

public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
    private Long assignedById;
    private Long assignedToId;

    public TaskDto() {
    }

    @JsonCreator
    public TaskDto(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("status") String status,
            @JsonProperty("assignedById") Long assignedById,
            @JsonProperty("assignedToId") Long assignedToId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.assignedById = assignedById;
        this.assignedToId = assignedToId;
    }

    public TaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.status = task.getStatus();
        this.assignedById = task.getAssignedById() != null ? task.getAssignedById().getUserId() : null;
        this.assignedToId = task.getAssignedToId() != null ? task.getAssignedToId().getUserId() : null;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long getAssignedById) {
        this.assignedById = getAssignedById;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long getAssignedToId) {
        this.assignedToId = getAssignedToId;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                ", assignedById=" + assignedById +
                ", assignedToId=" + assignedToId +
                '}';
    }
}
