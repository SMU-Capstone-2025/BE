package com.capstone.domain.log.service;

import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.entity.LogType;
import com.capstone.domain.log.repository.LogRepository;
import com.capstone.global.kafka.dto.ProjectChangePayload;
import com.capstone.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectLogService implements LogService<ProjectChangePayload>{

    private final LogRepository logRepository;

    @Override
    public void saveLogEntityFromPayload(String kafkaTopic, ProjectChangePayload payload) {
        LogEntity logEntity = LogEntity.builder()
                .email(payload.getModifiedBy())
                .method(kafkaTopic)
                .oldContent(payload.getOldContent())
                .newContent(payload.getNewContent())
                .targetType(LogType.PROJECT.getType())
                .targetId(payload.getId())
                .timestamp(DateTimeUtil.formatIsoDateTime(LocalDateTime.now()))
                .build();

        logRepository.save(logEntity);
    }
}
