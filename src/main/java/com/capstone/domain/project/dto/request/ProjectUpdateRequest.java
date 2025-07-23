package com.capstone.domain.project.dto.request;

import com.capstone.domain.project.entity.Project;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest (
        @Nullable
        String projectId,
        @NotNull
        @Size(min = 1)
        String projectName,
        @NotNull
        @Size(min = 1)
        String description,
        String imageId
){
}
