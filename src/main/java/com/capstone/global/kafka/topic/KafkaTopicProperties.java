package com.capstone.global.kafka.topic;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.kafka.topics")
@Getter
@Setter
public class KafkaTopicProperties {
    private String taskCreated;
    private String taskUpdated;
    private String taskDeleted;
    private String projectCreated;
    private String projectUpdated;
    private String projectDeleted;
    private String projectInvited;
    private String projectAuthenticated;

    private final Map<KafkaEventTopic, String> topicMap = new EnumMap<>(KafkaEventTopic.class);

    @PostConstruct
    public void initMap() {
        topicMap.put(KafkaEventTopic.TASK_CREATED, taskCreated);
        topicMap.put(KafkaEventTopic.TASK_UPDATED, taskUpdated);
        topicMap.put(KafkaEventTopic.TASK_DELETED, taskDeleted);
        topicMap.put(KafkaEventTopic.PROJECT_CREATED, projectCreated);
        topicMap.put(KafkaEventTopic.PROJECT_UPDATED, projectUpdated);
        topicMap.put(KafkaEventTopic.PROJECT_DELETED, projectDeleted);
        topicMap.put(KafkaEventTopic.PROJECT_INVITED, projectInvited);
        topicMap.put(KafkaEventTopic.PROJECT_AUTHENTICATED, projectAuthenticated);
    }

    public String resolve(KafkaEventTopic topic) {
        return topicMap.get(topic);
    }
}
