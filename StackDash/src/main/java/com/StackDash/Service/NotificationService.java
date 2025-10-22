package com.StackDash.Service;

import com.StackDash.Entity.Notification;
import com.StackDash.Entity.NotificationType;

import java.util.List;

public interface NotificationService {

    public List<Notification> getNotificationsForUser(Long userId);

    public void createNotification(Long userId, NotificationType type, String message);
}
