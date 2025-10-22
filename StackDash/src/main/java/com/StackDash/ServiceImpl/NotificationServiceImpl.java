package com.StackDash.ServiceImpl;

import com.StackDash.Entity.Notification;
import com.StackDash.Entity.NotificationType;
import com.StackDash.Repository.NotificationRepository;
import com.StackDash.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired

    private NotificationRepository notificationRepository;
    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public void createNotification(Long userId, NotificationType type, String message) {
        Notification notif = new Notification();
        notif.setUserId(userId);
        notif.setType(type);
        notif.setMessage(message);
        notif.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notif);
    }

}
