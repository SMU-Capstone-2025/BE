package com.capstone.domain.task.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    private String taskId;
    private String version;
    private String modifiedDateTime;
    private String modifiedBy;
    private String summary;
    private String content;
}

