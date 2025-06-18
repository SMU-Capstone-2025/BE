package com.capstone.global.kafka.dto.detail;

import com.capstone.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectChangeDetail extends ChangeDetail{
    private String title;
    private String description;
    private List<String> taskIds;
    private List<String> documentIds;

    public static ProjectChangeDetail from(Project project){
        return ProjectChangeDetail.builder()
                .title(project.getProjectName())
                .description(project.getDescription())
                .taskIds(project.getTaskIds())
                .documentIds(project.getDocumentIds())
                .build();
    }

}
