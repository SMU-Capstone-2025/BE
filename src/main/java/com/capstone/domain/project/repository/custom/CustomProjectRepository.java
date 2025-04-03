package com.capstone.domain.project.repository.custom;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.entity.Project;

import java.util.List;

public interface CustomProjectRepository {
    Project findByProjectName(String projectName);
    List<String> getAuthorityKeysByProjectId(String projectId);
    void updateAuthority(ProjectAuthorityRequest projectAuthorityRequest);
    List<Project> findAllById(List<String> ids);
}
