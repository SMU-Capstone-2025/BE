package com.capstone.domain.task.handler;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.global.kafka.dto.CommonChangePayload;
import com.capstone.global.kafka.dto.TaskChangePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.kafka.topic.KafkaTopicProperties;
import com.capstone.global.util.UrlGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.capstone.domain.notification.entity.Notification.createNotification;

@Component
@Slf4j
public class TaskUpdateHandler implements NotificationHandler<TaskChangePayload> {

    @Override
    public boolean canHandle(String kafkaTopic) {
        return KafkaEventTopic.TASK_UPDATED.getValue().equals(kafkaTopic);
    }

    @Override
    public String generateMessage(TaskChangePayload payload) {
        Map<String, Object> merged = new HashMap<>();
        merged.put("email", payload.getModifiedBy());
        merged.put("title", payload.getTitle());

        log.info("merged: {}", merged);

        return MessageGenerator.generateFromDto(MessageGenerator.TASK_UPDATED, merged);
    }


    @Override
    public String generateRedirectUrl(TaskChangePayload payload) {
        return UrlGenerator.createTaskUrl(payload.getId());
    }
}
