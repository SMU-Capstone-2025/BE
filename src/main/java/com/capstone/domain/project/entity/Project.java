package com.capstone.domain.project.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection= "project")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    private String id;
    private String projectName;
    private String description;
    private Map<String, String> authorities; // email:authority 구조
    private List<String> projectIds;
    private List<String> documentIds;
}
