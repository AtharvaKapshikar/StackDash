package com.StackDash.Controller;

import com.StackDash.Entity.Notification;
import com.StackDash.Service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notifications/")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserNotification(@PathVariable Long userId){
        try{
            log.trace("into notification controller method : {}",userId);
            List<Notification> notifications = notificationService.getNotificationsForUser(userId);
            List<Notification> latestNotification = notifications.stream().sorted(Collections.reverseOrder()).limit(10).collect(Collectors.toList());
            log.info("User notifications : {}",notifications);
            return ResponseEntity.ok(latestNotification);
        }catch (Exception ex){
            log.error("Error occurd while getting notification of user: {}", ex.getMessage());
            return ResponseEntity.status(405).body("Error occurd while getting notification of user: {}");
        }
    }
}
