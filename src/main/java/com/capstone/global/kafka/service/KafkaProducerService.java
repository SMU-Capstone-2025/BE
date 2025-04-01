package com.capstone.global.kafka.service;

import com.capstone.global.kafka.dto.RequestPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T, U> void sendTaskEvent(String topic, String action, T data, U email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<T, U> payload = new RequestPayload<>(email, "TASK", action, data);
            String message = objectMapper.writeValueAsString(payload);
            log.info("message: {}", message);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("Kafka 메시지 전송 실패", e);
        }
    }
    public <T, U> void sendProjectChangedEvent(String topic, String action, T data, U email) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RequestPayload<T, U> payload = new RequestPayload<>(email, "PROJECT", action, data);

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
