package com.capstone.global.kafka.service;

import com.capstone.global.kafka.topic.KafkaEventTopic;
import com.capstone.global.kafka.topic.KafkaTopicProperties;
import com.capstone.global.kafka.dto.RequestPayload;
import com.capstone.global.kafka.dto.TaskChangePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopics;

    public void sendEvent(KafkaEventTopic topicKey, Object payload) {
        String topicName = kafkaTopics.resolve(topicKey);
        kafkaTemplate.send(topicName, payload);
    }
}
