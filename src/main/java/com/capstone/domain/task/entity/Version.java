package com.capstone.domain.task.entity;

import com.capstone.global.entity.BaseDocument;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version extends BaseDocument{
    private String taskId;
    private String version;
    private String modifiedDateTime;
    private String modifiedBy;
    private String content;
    private List<String> attachmentList; // 사진, 표 등 첨부 파일 리스트.
}

