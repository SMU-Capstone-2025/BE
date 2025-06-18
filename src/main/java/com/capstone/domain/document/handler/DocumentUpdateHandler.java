package com.capstone.domain.document.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.dto.TaskChangePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.DocumentHandler;

import java.util.*;

@Component
@Slf4j
public class DocumentUpdateHandler implements NotificationHandler<TaskChangePayload> {
    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.TASK_UPDATED.equals(kafkaTopic);
    }

    @Override
    public String generateMessage(TaskChangePayload payload) {
        Map<String, Object> merged = new HashMap<>();
        merged.put("email", payload.getModifiedBy());
        merged.put("title", payload.getTitle());

        log.info("merged: {}", merged);

        return MessageGenerator.generateFromDto(MessageGenerator.DOCUMENT_UPDATED, merged);
    }

    @Override
    public List<String> findCoworkers(JsonNode rootNode) {
        return Collections.singletonList(rootNode.get("email").asText());
    }
}
