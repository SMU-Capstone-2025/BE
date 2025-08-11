package com.capstone.domain.document.dto;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DocumentResponse(
        String title,
        String status,
        String content,
        List<String> logs,
        List<String> attachments,
        String projectId,
        LocalDateTime updateTime
)
{
    public static DocumentResponse from(Document document) {
        return DocumentResponse.builder()
                .title(document.getTitle())
                .status(document.getStatus())
                .content(document.getContent())
                .logs(document.getLogs())
                .attachments(document.getAttachments())
                .projectId(document.getProjectId())
                .updateTime(document.getUpdatedAt())
                .build();
    }
}
