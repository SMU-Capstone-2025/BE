package com.capstone.global.kafka.consumer;

import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.elastic.repository.LogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LogConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "log-event", groupId = "log-group")
    public void consumeLogMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String method = jsonNode.get("method").asText();
            String email = jsonNode.get("email").asText();
            String data = jsonNode.get("data").toString(); // data 필드를 문자열로 유지

            LogEntity logEntity = LogEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .email(email)
                    .method(method)
                    .log(data)
                    .timestamp(LocalDateTime.now().toString())
                    .build();
            logRepository.save(logEntity);
            kafkaTemplate.send("notification-event", logEntity.toJson());
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}