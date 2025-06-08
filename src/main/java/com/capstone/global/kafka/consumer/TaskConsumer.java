package com.capstone.global.kafka.consumer;

import com.capstone.domain.log.service.TaskLogService;
import com.capstone.domain.notification.service.NotificationService;

import com.capstone.global.kafka.dto.TaskChangePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConsumer {
    private final TaskLogService logService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "task.created", groupId = "log-service", containerFactory = "taskKafkaListenerContainerFactory")
    public void processCreateLogSave(@Header("kafka_receivedTopic") String kafkaTopic,
                               @Payload TaskChangePayload payload) {
        try {
            logService.saveLogEntityFromPayload(kafkaTopic, payload);
        } catch (Exception e) {
            log.error("Failed to process Kafka message – topic={}, payload={}", kafkaTopic, payload, e);
        }
    }
    @KafkaListener(topics = "task.updated", groupId = "log-service", containerFactory = "taskKafkaListenerContainerFactory")
    public void processUpdateLogSave(@Header("kafka_receivedTopic") String kafkaTopic,
                               @Payload TaskChangePayload payload) {
        try {
            logService.saveLogEntityFromPayload(kafkaTopic, payload);
        } catch (Exception e) {
            log.error("Failed to process Kafka message – topic={}, payload={}", kafkaTopic, payload, e);
        }
    }
    @KafkaListener(topics = "task.deleted", groupId = "log-service", containerFactory = "taskKafkaListenerContainerFactory")
    public void processDeleteLogSave(@Header("kafka_receivedTopic") String kafkaTopic,
                               @Payload TaskChangePayload payload) {
        try {
            logService.saveLogEntityFromPayload(kafkaTopic, payload);
        } catch (Exception e) {
            log.error("Failed to process Kafka message – topic={}, payload={}", kafkaTopic, payload, e);
        }
    }

//    @KafkaListener(topics = "task.changed", groupId = "notification-service")
//    public void consumeUpdateMessage(String message){
//        try {
//            log.info("message2: {} ", message);
//            notificationService.processUpdateNotification(message);
//        } catch (Exception e) {
//            System.err.println("Failed to process log message: " + e.getMessage());
//        }
//    }
}
