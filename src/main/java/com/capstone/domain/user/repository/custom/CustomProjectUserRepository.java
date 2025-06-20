package com.capstone.domain.user.repository.custom;

import com.capstone.domain.project.dto.query.ProjectUserAuthority;
import com.capstone.domain.project.entity.Project;

import java.util.List;

public interface CustomProjectUserRepository {
    List<String> findUserIdByProjectId(String projectId);
    List<Project> findProjectsByUserId(String userId);
    List<ProjectUserAuthority> findUserAuthByProjectId(String projectId);
}
