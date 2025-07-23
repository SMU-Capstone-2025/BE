package com.capstone.domain.user.entity;

import com.capstone.global.entity.BaseDocument;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUser extends BaseDocument {
    @Id
    private String id;

    private String projectId;
    private String userId;

    private String role;
    private String joinedAt;

    public void updateRole(String role){
        this.role = role;
    }
}