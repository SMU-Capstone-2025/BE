package com.capstone.domain.log.entity;

import com.capstone.global.entity.BaseDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "logs")
@Builder
public class LogEntity extends BaseDocument {
    @Id
    private String id;
    private String email;
    private String method;
    private String oldContent;
    private String newContent;
    private String timestamp;
    private String taskId;
}