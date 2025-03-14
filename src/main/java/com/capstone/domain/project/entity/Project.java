package com.capstone.domain.project.entity;


import com.capstone.domain.project.dto.AuthorityRequest;
import com.capstone.domain.project.dto.ProjectDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public String getAuthority(String email){
        return (authorities != null) ? authorities.get(email) : null;
    }

    public static Project createProject(ProjectDto projectDto, Map<String, String> defaultAuthorities) {
        return Project.builder()
                .projectName(projectDto.getProjectName())
                .description(projectDto.getDescription())
                .authorities(defaultAuthorities)
                .projectIds(new ArrayList<>())
                .documentIds(new ArrayList<>())
                .build();
    }


    public void updateAllAuthorities(Map<String, String> newAuthorities) {
        newAuthorities.forEach(this::updateEachAuthority);
    }

    public void updateEachAuthority(String email, String role) {
        if (this.authorities.containsKey(email)) {
            this.authorities.replace(email, role);
        }
    }

}
