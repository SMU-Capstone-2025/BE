package com.capstone.global.kafka.consumer;

import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.mail.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectConsumer {
    private final MailService mailService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "project.changed", groupId = "mail-service")
    public void sendChangeEmail(String message) {
        mailService.processSendMessages(message);
    }

    @KafkaListener(topics = "project.changed", groupId = "notification-service")
    public void sendChangeNotification(String message) throws JsonProcessingException {
        notificationService.processUpdateNotification(message);
    }

}
