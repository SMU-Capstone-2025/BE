package com.capstone.domain.notification.controller;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/get")
    public ResponseEntity<List<Notification>> getNotificationLists(
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(notificationService.findAllNotifications(token));
    }

    @PutMapping("/read")
    public ResponseEntity<String> readNotification(@RequestParam("notificationId") String notificationId){
        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));
    }
}
