package com.capstone.domain.notification.service;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.exception.NotificationNotFoundException;
import com.capstone.domain.notification.handler.NotificationWebSocketHandler;
import com.capstone.domain.notification.message.NotificationMessages;
import com.capstone.domain.notification.repository.NotificationRepository;
import com.capstone.global.elastic.entity.LogEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final NotificationWebSocketHandler notificationWebSocketHandler;

    public Notification createNotification(String email, String content){
        return Notification.builder()
                .email(email)
                .content(content)
                .expiredDate(LocalDateTime.now().toString())
                .isRead(false)
                .build();
    }

    public List<Notification> findAllNotifications(String email){
        return notificationRepository.findAllByEmail(email);
    }

    public String saveNotification(Notification notification){
        notificationRepository.save(notification);
        return NotificationMessages.NOTIFICATION_SAVED;
    }

    public String markNotificationAsRead(String id){
        Notification notification = findNotificationByIdOrThrow(id);
        notification.setRead(true); // read 유무에 따라 프론트단에서 하이라이트를 다르게 하던지 할 필요가 있어보임.
        saveNotification(notification);

        return notification.getId(); // 프론트 단에서 변경된 알림의 아이디 값을 알면 하이라이트 변경을 알 수 있지 않을까?
    }

    public void deleteExpiredNotification(){
        List<Notification> notifications = notificationRepository.findAllByExpiredDateBefore(LocalDateTime.now().toString());
        notificationRepository.deleteAll(notifications);

    }

    public void processNotification(String message) {
        try {
            LogEntity log = objectMapper.readValue(message, LogEntity.class);
            Notification notification = createNotification(log.getEmail(), log.getLog());
            saveNotification(notification);
            notificationWebSocketHandler.broadcastMessage(notification);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Notification findNotificationByIdOrThrow(String id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationMessages.NOTIFICATION_NOT_FOUND));
    }
}

