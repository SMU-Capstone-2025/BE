package com.capstone.global.kafka.consumer;

import com.capstone.domain.log.service.LogService;
import com.capstone.domain.notification.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer
{
    private final ObjectMapper objectMapper;
    private final LogService logService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "document.changed", groupId = "log-service")
    public void processLogSave(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            log.info("jsonNode: {}", jsonNode);
            logService.saveLogEntityFromJsonNode(jsonNode);
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "document.changed", groupId = "notification-service")
    public void consumeUpdateMessage(String message){
        try {
            notificationService.processUpdateNotification(message);
        } catch (Exception e) {
            System.err.println("Failed to process log message: " + e.getMessage());
        }
    }
}
