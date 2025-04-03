package com.capstone.domain.task.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskDeleteHandler implements NotificationHandler {
    @Override
    public boolean canHandle(String method, String topic) {
        return "DELETE".equals(method) && "TASK".equals(topic);
    }

    @Override
    public String generateMessage(JsonNode rootNode) {
        String email = rootNode.get("email").asText();
        JsonNode data = rootNode.get("data");

        Map<String, Object> merged = new HashMap<>();
        merged.put("email", email);
        merged.put("title", data.get("title").asText());

        return MessageGenerator.generateFromDto(MessageGenerator.TASK_DELETED, merged);
    }

    @Override
    public List<String> findCoworkers(JsonNode rootNode) {
        List<String> coworkers = new ArrayList<>();
        JsonNode editorsNode = rootNode.get("data").get("editors");

        if (editorsNode != null && editorsNode.isArray()) {
            editorsNode.forEach(
                    jsonNode -> coworkers.add(jsonNode.asText())
            );
        }

        return coworkers;
    }
}
