package com.capstone.global.kafka.consumer;

import com.capstone.domain.document.service.DocumentChangeService;
import com.capstone.domain.log.service.TaskLogService;
import com.capstone.domain.notification.service.NotificationService;
import com.capstone.global.kafka.dto.DocumentChangePayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer {
    private final DocumentChangeService logService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "document.changed", groupId = "log-service", containerFactory = "documentChangeKafkaListenerContainerFactory")
    public void processLogSave(
            @Header("kafka_receivedTopic") String kafkaTopic,
            @Payload DocumentChangePayload payload) {
        try {
            logService.saveLogEntityFromPayload(kafkaTopic, payload);
        } catch (Exception e) {
            System.err.println("메시지 처리 실패: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "document.changed", groupId = "notification-service", containerFactory = "documentChangeKafkaListenerContainerFactory")
    public void consumeUpdateMessage(
            @Header("kafka_receivedTopic") String kafkaTopic,
            @Payload DocumentChangePayload payload){
        try {
            notificationService.processUpdateNotification(kafkaTopic, payload);
        } catch (Exception e) {
            System.err.println("Failed to process log message: " + e.getMessage());
        }
    }
}
