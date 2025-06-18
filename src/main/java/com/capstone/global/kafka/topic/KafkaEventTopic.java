package com.capstone.global.kafka.topic;

import lombok.Getter;

@Getter
public enum KafkaEventTopic {
    TASK_CREATED("task.created"),
    TASK_UPDATED("task.updated"),
    TASK_DELETED("task.deleted"),
    PROJECT_CREATED("project.created"),
    PROJECT_UPDATED("project.updated"),
    PROJECT_DELETED("project.deleted"),
    PROJECT_INVITED("project.invited"),
    PROJECT_AUTHENTICATED("project.authenticated");

    private final String value;

    KafkaEventTopic(String value) {
        this.value = value;
    }

}