package com.capstone.domain.task.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "task")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private String id;
    private String title;
    private String currentVersion;
    private String status;
    private List<String> editors;
    private List<Version> versionHistory;

    public void updateStatus(String status){
        this.status = status;
    }

    public void updateCurrentVersion(String currentVersion){
        this.currentVersion = currentVersion;
    }

    public void addNewVersion(Version version){
        this.versionHistory.add(version);
    }
}
