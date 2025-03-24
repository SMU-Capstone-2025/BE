package com.capstone.global.kafka.consumer;

import com.capstone.domain.log.service.LogService;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.repository.LogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final LogService logService;

    @KafkaListener(topics = "task.changed", groupId = "log-service")
    public void processLogSave(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            log.info("jsonNode: {}", jsonNode);
            LogEntity logEntity = logService.saveLogEntityFromJsonNode(jsonNode);
            kafkaTemplate.send("notification-event", logEntity.toJson());
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}
