package com.capstone.domain.document.dto;

import com.capstone.global.kafka.dto.detail.DocumentChangeDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DocumentLogDto(
        String email,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        DocumentChangeDetail oldContent,
        DocumentChangeDetail newContent
) {
}
