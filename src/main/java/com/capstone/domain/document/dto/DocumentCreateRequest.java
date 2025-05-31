package com.capstone.domain.document.dto;

import com.capstone.domain.document.entity.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record DocumentCreateRequest(
        @NotNull
        String title,
        @NotNull
        String projectId,
        @Nullable
        String content,
        @Nullable
        List<String> attachments,
        @Schema(description = "영문 값으로 기입 <br> PENDING: 진행 전, PROGRESS: 진행 중, COMPLETED : 진행 완료")
        String status
) {
    public Document to(String editorId){
        List<String> editors = new ArrayList<>();
        editors.add(editorId);

        return Document.builder()
                .projectId(this.projectId)
                .title(this.title)
                .content(this.content)
                .attachments(this.attachments)
                .logs(new ArrayList<>())
                .status(this.status)
                .editors(editors)
                .build();
    }
}
