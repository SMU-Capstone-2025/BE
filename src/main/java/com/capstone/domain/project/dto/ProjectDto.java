package com.capstone.domain.project.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @Nullable
    private String projectId;
    @NotNull
    @Size(min = 1)
    private String projectName;
    @NotNull
    @Size(min = 1)
    private String description;
    @Nullable
    private List<String> invitedEmails;
}
