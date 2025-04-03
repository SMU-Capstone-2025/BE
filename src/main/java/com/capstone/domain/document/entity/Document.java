package com.capstone.domain.document.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    @Id
    private String id;
    @Nullable
    private String documentId; // 문서 조회 시 이용되는 값.
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
