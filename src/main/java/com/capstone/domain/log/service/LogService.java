package com.capstone.domain.log.service;

import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.repository.LogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public LogEntity saveLogEntityFromJsonNode(JsonNode jsonNode){
        String taskId = null;

        try {
            JsonNode versionHistoryNode = jsonNode.path("data").path("versionHistory");
            if (versionHistoryNode.isArray() && versionHistoryNode.size() > 0) {
                JsonNode firstVersion = versionHistoryNode.get(0);
                JsonNode taskIdNode = firstVersion.get("taskId");
                if (taskIdNode != null && !taskIdNode.isNull()) {
                    taskId = taskIdNode.asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogEntity logEntity = LogEntity.builder()
                .email(jsonNode.path("email").asText())
                .method(jsonNode.path("method").asText())
                .log(jsonNode.path("data").toString())
                .taskId(taskId)
                .timestamp(LocalDateTime.now().toString())
                .build();

        logRepository.save(logEntity);

        System.out.println("Saved LogEntity: " + logEntity);
        return logEntity;
    }

}
