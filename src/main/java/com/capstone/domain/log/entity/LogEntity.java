package com.capstone.domain.log.entity;

import com.capstone.global.entity.BaseDocument;

import com.capstone.global.kafka.dto.detail.ChangeDetail;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logs")
@Builder
public class LogEntity extends BaseDocument {
    @Id
    private String id;
    private String email;
    private String targetType;
    private String targetId;
    private String method;
    private ChangeDetail oldContent;
    private ChangeDetail newContent;
    private String timestamp;

}