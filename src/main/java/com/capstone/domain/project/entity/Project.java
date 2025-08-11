package com.capstone.domain.project.entity;

import com.capstone.global.entity.BaseDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection= "project")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseDocument {
    @Id
    private String id;
    private String projectName;
    private String imageId;
    private String description;
    private List<String> taskIds;
    private List<String> documentIds;

    public void updateProjectInfoWithImage(String projectName, String description,String imageId) {
        this.projectName = projectName;
        this.description = description;
        this.imageId = imageId;

    }
    public void updateProjectInfo(String projectName, String description) {
        this.projectName = projectName;
        this.description = description;
    }

    public void addNewTaskId(String taskId){
        if(this.taskIds == null) {
            this.taskIds = new ArrayList<>();
        }
        if (!this.taskIds.contains(taskId)) {
            this.taskIds.add(taskId);
        }
    }
}
