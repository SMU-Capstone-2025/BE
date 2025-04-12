package com.capstone.domain.project.dto.response;

import com.capstone.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProjectResponse(
        String name,
        String description,
        List<String> coworkers
) {
    public static ProjectResponse from(Project project, List<String> emails){
        return ProjectResponse.builder()
                .name(project.getProjectName())
                .description(project.getDescription())
                .coworkers(emails)
                .build();
    }
}
