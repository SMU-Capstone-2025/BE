package com.capstone.global.kafka.listener;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.kafka.dto.RequestPayload;
import com.capstone.global.kafka.dto.ResponsePayload;
import com.capstone.global.kafka.message.LogMessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LogListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "log-event", groupId = "log-group")
    public void consumeLogMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String taskId = jsonNode.get("taskId").asText();
            String method = jsonNode.get("method").asText();
            String data = jsonNode.get("data").toString(); // data 필드를 문자열로 유지
            String response = String.format("Processed task: %s with method: %s", taskId, method);

            // Elasticsearch에 저장할 엔티티 생성
            LogEntity logEntity = new LogEntity();
            logEntity.setId(UUID.randomUUID().toString()); // 고유 ID
            logEntity.setTaskId(taskId);
            logEntity.setMethod(method);
            logEntity.setLogMessage(data);
            logEntity.setTimestamp(LocalDateTime.now().toString());

            //kafkaTemplate.send("response-topic", response);

        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }
}