package com.capstone.domain.project.dto.request;

import com.capstone.domain.project.entity.Project;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ProjectSaveRequest(
        @Nullable
        String projectId,
        @NotNull
        @Size(min = 1)
        String projectName,
        @NotNull
        @Size(min = 1)
        String description,
        @Nullable
        List<String> invitedEmails
){
    public Project toProject(Map<String, String> defaultAuthorities){
        return Project.builder()
                .projectName(this.projectName())
                .description(this.description())
                .authorities(defaultAuthorities)
                .taskIds(new ArrayList<>())
                .documentIds(new ArrayList<>())
                .build();
    }
}
