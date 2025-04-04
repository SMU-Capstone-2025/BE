package com.capstone.domain.task.dto.request;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.message.Status;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public record TaskRequest(String id,
                          String title,
                          String modifiedBy,
                          String version,
                          String summary,
                          String content,
                          @Nullable
                       List<String> editors
                       ){

    public Task toTask() {
        return Task.builder()
                .title(this.title())
                .status(Status.IN_PROGRESS)
                .currentVersion(this.version)
                .versionHistory(new ArrayList<>())
                .editors(editors)
                .build();
    }
}
