package com.capstone.domain.project.handler;

import com.capstone.domain.notification.handler.NotificationHandler;

import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.dto.ProjectInvitePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.capstone.global.kafka.topic.KafkaEventTopic;

import com.capstone.global.util.UrlGenerator;
import org.springframework.stereotype.Component;


@Component
public class ProjectInviteHandler implements NotificationHandler<ProjectInvitePayload> {

    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.PROJECT_INVITED.getValue().equals(kafkaTopic);
    }

    @Override
    public String generateMessage(ProjectInvitePayload payload) {
        return MessageGenerator.generateFromDto(MessageGenerator.PROJECT_INVITED, payload);
    }

    @Override
    public String generateRedirectUrl(ProjectInvitePayload payload) {
        return UrlGenerator.createProjectUrl(payload.getId());
    }
}
