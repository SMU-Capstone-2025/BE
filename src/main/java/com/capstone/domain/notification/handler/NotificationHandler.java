package com.capstone.domain.notification.handler;

import com.capstone.global.kafka.dto.CommonChangePayload;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface NotificationHandler<T extends CommonChangePayload> {
    boolean canHandle(String kafkaTopic);
    String generateMessage(T payload);
    List<String> findCoworkers(JsonNode rootNode);
}
