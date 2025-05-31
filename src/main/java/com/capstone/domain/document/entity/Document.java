package com.capstone.domain.document.entity;

import com.capstone.domain.document.dto.DocumentEditVo;
import com.capstone.global.entity.BaseDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document extends BaseDocument {
    @Id
    private String id;

    private String projectId;

    private String status;

    private String title;

    private String content;

    private List<String> logs;

    private List<String> attachments;

    private List<String> editors;


    public void update(String email, String key, String projectId, DocumentEditVo vo){
        this.id = key;
        this.projectId = projectId;
        if (vo.getTitle() != null) this.title = vo.getTitle();
        if (vo.getStatus() != null) this.status = vo.getStatus();
        if (vo.getLogs() != null) this.logs = vo.getLogs();
        if (vo.getAttachments() != null) this.attachments = vo.getAttachments();
        if (vo.getContent() != null) this.content = vo.getContent();

        if (this.editors == null) {
            this.editors = new ArrayList<>();
        }

        this.editors.add(email);

        this.updatedAt = LocalDateTime.now();
    }

}
