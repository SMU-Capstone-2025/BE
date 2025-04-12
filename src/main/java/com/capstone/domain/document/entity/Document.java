package com.capstone.domain.document.entity;

import com.capstone.global.entity.BaseDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;

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
    @Nullable
    private String content;
    @Nullable
    private List<String> logs;
    @Nullable
    private List<String> attachments;

    public Document(String id, String content){
        this.id = id;
        this.content = content;
    }

}
