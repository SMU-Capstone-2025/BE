package com.capstone.domain.project.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.kafka.topic.KafkaTopicProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ProjectAuthHandler implements NotificationHandler<ProjectChangePayload> {

    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.PROJECT_AUTHENTICATED.getValue().equals(kafkaTopic);
    }


    @Override
    public String generateMessage(ProjectChangePayload payload) {

        Map<String, Object> merged = Map.of(
                "email", payload.getCoworkers(),
                "title", payload.getTitle()
        );

        return MessageGenerator.generateFromDto(MessageGenerator.PROJECT_AUTHENTICATED, merged);
    }
}
