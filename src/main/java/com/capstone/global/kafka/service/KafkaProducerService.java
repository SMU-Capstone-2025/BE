package com.capstone.global.kafka.service;

import com.capstone.domain.task.dto.TaskDto;
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

    public void sendTaskEvent(String topic, String action, TaskDto taskDto, String email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<TaskChangePayload> payload = new RequestPayload<>(
                    email,
                    action,
                    new TaskChangePayload(taskDto.id(), taskDto.title(), taskDto.modifiedBy(), taskDto.version()
                            ,taskDto.summary(), taskDto.content())
            );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendProjectEvent(String topic, String action, String projectName, Map<String, String> authorities) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<Map<String, String>> payload = new RequestPayload<>(

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
                    null
                    );
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}