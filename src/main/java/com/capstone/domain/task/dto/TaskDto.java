package com.capstone.domain.task.dto;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.message.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

public record TaskDto (String id,
                       String title,
                       String modifiedBy,
                       String version,
                       String summary,
                       String content){

    public Task toTask(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.title())
                .status(Status.IN_PROGRESS)
                .currentVersion(taskDto.version)
                .versionHistory(new ArrayList<>())
                .build();
    }
}
