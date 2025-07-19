package com.capstone.domain.project.dto.response;

import com.capstone.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProjectResponse(
        String projectId,
        String name,
        String description,
        List<ProjectCoworkerDto> coworkers
) {
    public static ProjectResponse from(Project project, List<ProjectCoworkerDto> coworkers) {
        return ProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .coworkers(coworkers)
                .build();
    }
}
