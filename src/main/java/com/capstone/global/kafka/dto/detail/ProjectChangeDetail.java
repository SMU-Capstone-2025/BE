package com.capstone.global.kafka.dto.detail;

import com.capstone.domain.project.dto.query.ProjectUserAuthority;
import com.capstone.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectChangeDetail extends ChangeDetail<List<ProjectUserAuthority>>{
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

    // 권한 조정 및 초대 시 사용.
    public static ProjectChangeDetail from(List<ProjectUserAuthority> coworkers){
        return ProjectChangeDetail.builder()
                .coworkers(coworkers)
                .build();
    }

}
