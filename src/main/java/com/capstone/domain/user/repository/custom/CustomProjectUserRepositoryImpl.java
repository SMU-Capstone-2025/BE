package com.capstone.domain.user.repository.custom;

import com.capstone.domain.project.entity.Project;
import com.capstone.domain.user.entity.ProjectUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomProjectUserRepositoryImpl implements CustomProjectUserRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findUserIdByProjectId(String projectId) {
        Query query = new Query(Criteria.where("projectId").is(projectId));
        query.fields().include("userId"); // 또는 "email" 필드

        List<ProjectUser> projectUsers = mongoTemplate.find(query, ProjectUser.class);
        return projectUsers.stream()
                .map(ProjectUser::getUserId) // 또는 getEmail(), 실제 필드명에 따라 변경
                .toList();
    }
    @Override
    public List<Project> findProjectsByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));

        List<ProjectUser> projectUsers = mongoTemplate.find(query, ProjectUser.class);
        log.info(projectUsers.toString());
        List<String> projectIds = projectUsers.stream()
                .map(ProjectUser::getProjectId)
                .toList();
        if (projectIds.isEmpty()) {
            return List.of();
        }
        Query projectQuery = new Query(Criteria.where("_id").in(projectIds));
        return mongoTemplate.find(projectQuery, Project.class);
    }

}
