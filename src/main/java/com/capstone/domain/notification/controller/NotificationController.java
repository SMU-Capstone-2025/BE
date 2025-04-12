package com.capstone.domain.notification.controller;

import com.capstone.docs.NotificationControllerDocs;
import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerDocs {
    private final NotificationService notificationService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotificationLists(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.findAllNotifications(token)));
    }

    @PutMapping("/read")
    public ResponseEntity<ApiResponse<String>> readNotification(@RequestParam("notificationId") String notificationId){
        return ResponseEntity.ok(ApiResponse.onSuccess(notificationService.markNotificationAsRead(notificationId)));
    }
}
