package com.capstone.domain.project.repository.custom;

import com.capstone.domain.project.entity.Project;

import java.util.List;

public interface CustomProjectRepository {
    Project findByProjectName(String projectName);
    List<String> getAuthorityKeysByProjectId(String projectId);
}
