package com.capstone.global.kafka.service;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.global.kafka.dto.RequestPayload;
import com.capstone.global.kafka.dto.TaskChangePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T> void sendTaskEvent(String topic, String action, T data, String email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<T> payload = new RequestPayload<>(email, "TASK", action, data);

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("Kafka 메시지 전송 실패", e);
        }
    }
    public void sendProjectChangedEvent(String action, String projectName, Map<String, String> authorities, List<String> emails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<ProjectChangePayload> payload = new RequestPayload<>(
                    null,
                    action,
                    new ProjectChangePayload(projectName, authorities, emails)
            );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("project.changed", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
