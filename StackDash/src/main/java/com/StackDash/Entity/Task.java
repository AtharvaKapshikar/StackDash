package com.StackDash.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "StackDash_Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;
    private String status; // e.g., "Pending", "Completed"
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assigned_by_id")
    private User assignedById;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedToId;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(User assignedById) {
        this.assignedById = assignedById;
    }

    public User getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(User assignedToId) {
        this.assignedToId = assignedToId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", AssignedById=" + assignedById +
                ", AssignedToId=" + assignedToId +
                '}';
    }
}
