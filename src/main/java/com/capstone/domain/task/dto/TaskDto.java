package com.capstone.domain.task.dto;

import jakarta.annotation.Nullable;
import lombok.Data;


@Data
public class TaskDto {
    @Nullable // 신규 작성 시에는 없음.
    private String id;
    private String title;
    private String modifiedBy;
    private String version;
    private String summary;
    private String content;
}
