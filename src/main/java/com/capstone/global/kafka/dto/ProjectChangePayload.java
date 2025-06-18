package com.capstone.global.kafka.dto;

import com.capstone.domain.project.entity.Project;
import com.capstone.global.kafka.dto.detail.ProjectChangeDetail;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectChangePayload extends CommonChangePayload {
    private String id;
    private String projectName;
    private String modifiedBy;
    private ProjectChangeDetail oldContent;
    private ProjectChangeDetail newContent;
    private List<String> coworkers;

    public static ProjectChangePayload from(Project project, ProjectChangeDetail oldContent,
                                            ProjectChangeDetail newContent, String email,
                                            List<String> coworkers) {
        return ProjectChangePayload.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .modifiedBy(email)
                .oldContent(oldContent)
                .newContent(newContent)
                .coworkers(coworkers)
                .build();
    }
}