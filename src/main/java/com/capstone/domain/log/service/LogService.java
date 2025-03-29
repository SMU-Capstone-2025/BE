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
        LogEntity logEntity = LogEntity.builder()
                .email(jsonNode.get("email").toString())
                .method(jsonNode.get("method").toString())
                .log(jsonNode.get("data").toString())
                .timestamp(LocalDateTime.now().toString())
                .build();

        logRepository.save(logEntity);
        return logEntity;
    }
}
