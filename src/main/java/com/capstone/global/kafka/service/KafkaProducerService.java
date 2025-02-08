package com.capstone.global.kafka.service;

import com.capstone.domain.task.dto.TaskDto;
import com.capstone.global.kafka.dto.RequestPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    taskDto.getId(),
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

    public void sendProjectEvent(String topic, String action, String projectId, List<String> emails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<List<String>> payload = new RequestPayload<>(
                    projectId,
                    null,
                    action,
                    emails
                    );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMailEvent(String topic, List<String> emails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<List<String>> payload = new RequestPayload<>(
                    null,
                    null,
                    null,
                    emails
                    );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}