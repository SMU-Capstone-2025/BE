package notification.controller;

import lombok.RequiredArgsConstructor;
import notification.entity.Notification;
import notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    //TODO: 시큐리티 도입 시 헤더로 토큰을 받아온 후 이메일 추출하는 방식으로 변경.
    @GetMapping("/get")
    public ResponseEntity<List<Notification>> getNotificationLists(@RequestParam("email") String email){
        return ResponseEntity.ok(notificationService.findAllNotifications(email));
    }

    @PutMapping("/read")
    public ResponseEntity<String> readNotification(@RequestParam("notificationId") String notificationId){
        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));
    }
}
