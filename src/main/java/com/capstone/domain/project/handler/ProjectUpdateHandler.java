package com.capstone.domain.project.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.domain.project.entity.Project;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.kafka.topic.KafkaTopicProperties;
import com.capstone.global.util.UrlGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProjectUpdateHandler implements NotificationHandler<ProjectChangePayload> {

    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.PROJECT_UPDATED.getValue().equals(kafkaTopic);
    }

    @Override
    public String generateMessage(ProjectChangePayload payload) {
        Map<String, Object> merged = Map.of(
                "projectName", payload.getTitle()
        );

        return MessageGenerator.generateFromDto(MessageGenerator.PROJECT_UPDATED, merged);
    }

    @Override
    public String generateRedirectUrl(ProjectChangePayload payload) {
        return UrlGenerator.createProjectUrl(payload.getId());
    }
}
