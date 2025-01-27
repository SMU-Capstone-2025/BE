package com.capstone.global.kafka.consumer;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.handler.NotificationWebSocketHandler;
import com.capstone.domain.notification.repository.NotificationRepository;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.elastic.entity.LogEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "notification-event", groupId = "notification-group")
    public void consumeNotificationMessage(String message){
        try {
            notificationService.processNotification(message);
        } catch (Exception e) {
            System.err.println("Failed to process log message: " + e.getMessage());
        }
    }
}
