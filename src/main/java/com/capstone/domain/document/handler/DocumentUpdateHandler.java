package com.capstone.domain.document.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.dto.DocumentChangePayload;
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
public class DocumentUpdateHandler implements NotificationHandler<DocumentChangePayload> {
    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.DOCUMENT_UPDATED.getValue().equals(kafkaTopic);
    }

    @Override
    public String generateMessage(DocumentChangePayload payload) {
        Map<String, Object> merged = new HashMap<>();
        merged.put("email", payload.getModifiedBy());
        merged.put("title", payload.getTitle());

        return MessageGenerator.generateFromDto(MessageGenerator.DOCUMENT_UPDATED, merged);
    }
}
