package com.capstone.domain.document.service;

import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.entity.LogType;
import com.capstone.domain.log.repository.LogRepository;
import com.capstone.domain.log.service.LogService;
import com.capstone.global.kafka.dto.DocumentChangePayload;
import com.capstone.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentChangeService implements LogService<DocumentChangePayload> {

    private final LogRepository logRepository;

    @Override
    public void saveLogEntityFromPayload(String kafkaTopic, DocumentChangePayload payload) {
        LogEntity logEntity = LogEntity.builder()
                .email(payload.getModifiedBy())
                .method(kafkaTopic)
                .oldContent(payload.getOldContent())
                .newContent(payload.getNewContent())
                .targetType(LogType.DOCUMENT.getType())
                .targetId(payload.getId())
                .timestamp(DateTimeUtil.formatIsoDateTime(LocalDateTime.now()))
                .build();

        logRepository.save(logEntity);
    }
}
