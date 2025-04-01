package com.capstone.domain.task.handler;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.capstone.domain.notification.entity.Notification.createNotification;

@Component
@Slf4j
public class TaskUpdateHandler implements NotificationHandler {

    @Override
    public boolean canHandle(String method, String topic) {
        return "UPDATE".equals(method) && "TASK".equals(topic);
    }

    @Override
    public String generateMessage(JsonNode rootNode) {
        String email = rootNode.get("email").asText();
        JsonNode data = rootNode.get("data");

        Map<String, Object> merged = new HashMap<>();
        merged.put("email", email);
        merged.put("title", data.get("title").asText());

        log.info("merged: {}", merged);

        return MessageGenerator.generateFromDto(MessageGenerator.TASK_UPDATED, merged);
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
