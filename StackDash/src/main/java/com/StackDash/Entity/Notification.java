package com.StackDash.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Stack_Dash_notifications")
public class Notification implements Comparable<Notification> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId; // Link to the user who receives the notification

    private NotificationType type; // e.g. registered, verified, task, password

    private String message;

    private LocalDateTime timestamp;

    // Optional: status, severity, read/unread flags


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int compareTo(Notification notify) {
        return this.timestamp.compareTo(notify.timestamp);
    }
}

