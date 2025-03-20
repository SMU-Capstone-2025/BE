package com.capstone.global.kafka.consumer;

import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.elastic.repository.LogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "task.changed", groupId = "log-service")
    public void consumeLogMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            log.info("jsonNode: {}", jsonNode);
            LogEntity logEntity = LogEntity.builder()
                    .email(jsonNode.get("email").toString())
                    .method(jsonNode.get("method").toString())
                    .log(jsonNode.get("data").toString())
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            logRepository.save(logEntity);
            kafkaTemplate.send("notification-event", logEntity.toJson());
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}
