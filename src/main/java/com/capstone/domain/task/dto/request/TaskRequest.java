package com.capstone.domain.task.dto.request;

import com.capstone.domain.task.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record TaskRequest(String taskId,
                          @NotNull
                          String projectId,
                          @NotNull
                          @Schema(description = "영문 값으로 기입 <br> PENDING: 진행 전, PROGRESS: 진행 중, COMPLETED : 진행 완료")
                          String status,
                          @NotNull
                          String title,
                          @NotNull
                          String modifiedBy,
                          @NotNull
                          String version,
                          @NotNull
                          String content,
                          @Nullable
                          List<String> coworkers,
                          @Nullable
                          String deadline
                    ){

    public Task toTask() {
        List<String> updatedEditors = coworkers != null ? new ArrayList<>(coworkers) : new ArrayList<>();
        if (!updatedEditors.contains(modifiedBy)) {
            updatedEditors.add(modifiedBy);
        }
        return Task.builder()
                .title(this.title())
                .projectId(this.projectId)
                .status(status)
                .currentVersion(this.version)
                .versionHistory(new ArrayList<>())
                .coworkers(coworkers)
                .deadline(LocalDate.parse(this.deadline))
                .build();
    }
}
