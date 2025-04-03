package com.capstone.domain.notification.handler;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface NotificationHandler {
    boolean canHandle(String method, String topic);
    String generateMessage(JsonNode rootNode);
    List<String> findCoworkers(JsonNode rootNode);
}
