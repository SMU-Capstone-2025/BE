package com.capstone.domain.user.repository.custom;

import com.capstone.domain.user.entity.ProjectUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
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
}
