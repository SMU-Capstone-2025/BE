package com.capstone.global.kafka.dto;

import com.capstone.domain.project.entity.Project;
import com.capstone.global.kafka.dto.detail.ProjectChangeDetail;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectChangePayload extends CommonChangePayload {
    private ProjectChangeDetail oldContent;
    private ProjectChangeDetail newContent;

    public static ProjectChangePayload from(Project project, ProjectChangeDetail oldContent,
                                            ProjectChangeDetail newContent, String email,
                                            List<String> coworkers) {
        return ProjectChangePayload.builder()
                .id(project.getId())
                .title(project.getProjectName())
                .modifiedBy(email)
                .oldContent(oldContent)
                .newContent(newContent)
                .coworkers(coworkers)
                .build();
    }
}