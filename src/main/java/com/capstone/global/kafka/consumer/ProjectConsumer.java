package com.capstone.global.kafka.consumer;

import com.capstone.domain.log.service.ProjectLogService;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.mail.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectConsumer {
    private final ProjectLogService projectLogService;
    private final MailService mailService;
    private final NotificationService notificationService;


    @KafkaListener(
            topics = {"project.updated", "project.deleted", "project.created"},
            groupId = "notification-service",
            containerFactory = "projectKafkaListenerContainerFactory"
    )
    public void processCreateLogSave(
            @Header("kafka_receivedTopic") String kafkaTopic,
            @Payload ProjectChangePayload payload
            ) throws JsonProcessingException {
        projectLogService.saveLogEntityFromPayload(kafkaTopic, payload);
    }

    @KafkaListener(
            topics = {"project.updated", "project.deleted", "project.created"},
            groupId = "mail-service",
            containerFactory = "projectKafkaListenerContainerFactory"
    )
    public void sendChangeEmail(@Header("kafka_receivedTopic") String kafkaTopic,
            @Payload ProjectChangePayload payload) throws JsonProcessingException {
        mailService.sendMultipleMessages(payload.getCoworkers());
    }

    @KafkaListener(
            topics = {"project.updated", "project.deleted", "project.created"},
            groupId = "notification-service",
            containerFactory = "projectKafkaListenerContainerFactory"
    )
    public void sendChangeNotification(@Header("kafka_receivedTopic") String kafkaTopic,
            @Payload ProjectChangePayload payload) throws JsonProcessingException {
        notificationService.processUpdateNotification(kafkaTopic, payload);
    }

}
