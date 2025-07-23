package com.capstone.global.kafka.consumer;

import com.capstone.domain.log.service.ProjectLogService;
import com.capstone.domain.mail.service.ProjectAuthMailService;
import com.capstone.domain.mail.service.ProjectInviteMailService;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.kafka.dto.ProjectAuthPayload;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.domain.mail.service.RegisterMailService;
import com.capstone.global.kafka.dto.ProjectInvitePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectConsumer {
    private final ProjectLogService projectLogService;
    private final RegisterMailService mailService;
    private final ProjectAuthMailService authMailService;
    private final ProjectInviteMailService inviteMailService;
    private final NotificationService notificationService;


//    @KafkaListener(
//            topics = {"project.updated", "project.deleted", "project.created", "project.invited", "project.authenticated"},
//            groupId = "log-service",
//            containerFactory = "projectKafkaListenerContainerFactory"
//    )
//    public void processCreateLogSave(
//            @Header("kafka_receivedTopic") String kafkaTopic,
//            @Payload ProjectChangePayload payload
//            ) throws JsonProcessingException {
//        projectLogService.saveLogEntityFromPayload(kafkaTopic, payload);
//    }

    @KafkaListener(
            topics = {"project.invited"},
            groupId = "mail-service",
            containerFactory = "projectInviteKafkaListenerContainerFactory"
    )
    public void sendInviteEmail(@Header("kafka_receivedTopic") String kafkaTopic,
            @Payload ProjectInvitePayload payload) throws JsonProcessingException {
        try {
            inviteMailService.sendSimpleMessage(payload.getInvitor(), payload.getInvitee(), payload.getProjectId(), payload.getProjectName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(
            topics = {"project.authenticated"},
            groupId = "mail-service",
            containerFactory = "projectAuthKafkaListenerContainerFactory"
    )
    public void sendAuthChangeEmail(@Payload ProjectAuthPayload payload) throws JsonProcessingException {
        try {
            authMailService.sendSimpleMessage(payload.getUserEmail(), payload.getProjectName(), payload.getNewRole());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @KafkaListener(
//            topics = {"project.updated", "project.deleted", "project.created", "project.invited", "project.authenticated"},
//            groupId = "notification-service",
//            containerFactory = "projectKafkaListenerContainerFactory"
//    )
//    public void sendChangeNotification(@Header("kafka_receivedTopic") String kafkaTopic,
//            @Payload ProjectChangePayload payload) throws JsonProcessingException {
//        notificationService.processUpdateNotification(kafkaTopic, payload);
//    }

}
