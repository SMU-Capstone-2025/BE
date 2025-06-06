package com.capstone.domain.project.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ProjectUpdateHandler implements NotificationHandler {
    @Override
    public boolean canHandle(String method, String topic) {
        return "UPDATE".equals(method) && "PROJECT".equals(topic);
    }

    @Override
    public String generateMessage(JsonNode rootNode) {
        Map<String, Object> merged = Map.of(
                "projectName", rootNode.get("data").get("name").asText()
        );

        return MessageGenerator.generateFromDto(MessageGenerator.PROJECT_UPDATED, merged);
    }

    @Override
    public List<String> findCoworkers(JsonNode rootNode) {
        return Collections.singletonList(rootNode.get("email").asText());
    }
}
