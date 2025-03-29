package com.capstone.domain.task.dto;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.message.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record TaskDto (String id,
                       String title,
                       String modifiedBy,
                       String version,
                       String summary,
                       String content,
                       @Nullable
                       List<String> editors,
                       @Nullable LocalDate deadline
                       ) {

    public Task toTask() {
        return Task.builder()
                .title(this.title())
                .status(Status.IN_PROGRESS)
                .currentVersion(this.version)
                .versionHistory(new ArrayList<>())
                .editors(editors)
                .deadline(deadline)
                .build();
    }
}
