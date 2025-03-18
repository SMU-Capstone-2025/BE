package com.capstone.global.kafka.consumer;

import com.capstone.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "notification-event", groupId = "notification-group")
    public void consumeLogMessage(String message){
        try {
            notificationService.processLogNotification(message);
        } catch (Exception e) {
            System.err.println("Failed to process log message: " + e.getMessage());
        }
    }

}
