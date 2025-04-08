package com.capstone.domain.task.dto.request;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.message.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record TaskRequest(String id,
                          @NotNull
                          String title,
                          @NotNull
                          String modifiedBy,
                          @NotNull
                          String version,
                          @NotNull
                          String summary,
                          @NotNull
                          String content,
                          @Nullable
                          List<String> editors,
                          @NotNull
                          LocalDate deadline
                    ){

    public Task toTask() {
        List<String> updatedEditors = editors != null ? new ArrayList<>(editors) : new ArrayList<>();
        if (!updatedEditors.contains(modifiedBy)) {
            updatedEditors.add(modifiedBy);
        }
        return Task.builder()
                .title(this.title())
                .status(Status.IN_PROGRESS)
                .currentVersion(this.version)
                .versionHistory(new ArrayList<>())
                .editors(editors)
                .deadline(this.deadline)
                .build();
    }
}
