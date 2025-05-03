package com.capstone.domain.document.dto;

import com.capstone.domain.document.entity.Document;
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
        List<String> attachments
) {
    public Document to(){
        return Document.builder()
                .projectId(this.projectId)
                .title(this.title)
                .content(this.content)
                .attachments(this.attachments)
                .logs(new ArrayList<>())
                .build();
    }
}
