package com.capstone.global.kafka.service;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.kafka.dto.RequestPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskEvent(String topic, String action, TaskDto taskDto, String email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> data = new HashMap<>();
            data.put("version", taskDto.getVersion());
            data.put("content", taskDto.getContent());

            @SuppressWarnings("unchecked")
            Map<String, Object>[] requestData = new Map[]{data};

            RequestPayload<Map<String, Object>[]> payload = new RequestPayload<>(
                    email,
                    action,
                    requestData
            );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
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