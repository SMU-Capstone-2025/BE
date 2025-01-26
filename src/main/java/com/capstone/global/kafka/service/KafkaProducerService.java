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

    public void sendTaskEvent(String topic, String action, TaskDto taskDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // requestData에 들어갈 데이터를 배열로 생성
            Map<String, Object> data = new HashMap<>();
            data.put("version", taskDto.getVersion());
            data.put("content", taskDto.getContent());

            // 배열 생성
            @SuppressWarnings("unchecked")
            Map<String, Object>[] requestData = new Map[]{data};

            // RequestPayload 생성
            RequestPayload<Map<String, Object>[]> payload = new RequestPayload<>(
                    taskDto.getId(),  // taskId
                    action,           // method
                    requestData       // requestData 배열
            );

            // JSON 직렬화
            String message = objectMapper.writeValueAsString(payload);
            System.out.println("Produced message: " + message);

            // Kafka로 전송
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}