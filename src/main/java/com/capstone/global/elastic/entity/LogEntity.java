package com.capstone.global.elastic.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "logs") // Elasticsearch 인덱스 이름
public class LogEntity {
    @Id
    private String id;
    private String taskId;
    private String method;
    private String logMessage;
    private String timestamp;
}