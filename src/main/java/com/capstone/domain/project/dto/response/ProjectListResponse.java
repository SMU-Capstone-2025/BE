package com.capstone.domain.project.dto.response;

import com.capstone.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProjectListResponse(
        String name,
        String description,
        Map<String,String> coworkers
) {
    public static ProjectListResponse from(Project project){
        return ProjectListResponse.builder()
                .name(project.getProjectName())
                .description(project.getDescription())
                .coworkers(project.getAuthorities())
                .build();
    }
}
