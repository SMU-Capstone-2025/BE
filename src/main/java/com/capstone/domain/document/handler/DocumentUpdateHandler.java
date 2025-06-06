package com.capstone.domain.document.handler;

import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.DocumentHandler;

import java.util.*;

@Component
@Slf4j
public class DocumentUpdateHandler implements NotificationHandler
{
    @Override
    public boolean canHandle(String method, String topic) {
        {
            return "UPDATE".equals(method) && "DOCUMENT".equals(topic);
        }
    }

    @Override
    public String generateMessage(JsonNode rootNode) {
        String email = rootNode.get("email").asText();
        JsonNode data = rootNode.get("data");

        Map<String, Object> merged = new HashMap<>();
        merged.put("email", email);
        merged.put("title", data.get("title").asText());

        log.info("merged: {}", merged);

        return MessageGenerator.generateFromDto(MessageGenerator.DOCUMENT_UPDATED, merged);
    }

    @Override
    public List<String> findCoworkers(JsonNode rootNode) {
        return Collections.singletonList(rootNode.get("email").asText());
    }
}
