package com.capstone.domain.project.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
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
        List<String> email = Collections.singletonList(rootNode.get("email").asText());
        JsonNode data = rootNode.get("data");

        Map<String, Object> merged = Map.of(
                "email", Collections.singletonList(rootNode.get("email").asText()),
                "title", rootNode.get("data").get("projectName").asText()
        );

        return MessageGenerator.generateFromDto(MessageGenerator.PROJECT_UPDATED, merged);
    }

    @Override
    public List<String> findCoworkers(JsonNode rootNode) {
        return Collections.singletonList(rootNode.get("email").asText());
    }
}
