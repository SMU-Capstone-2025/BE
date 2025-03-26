package com.capstone.domain.project.repository.custom;

import com.capstone.domain.project.dto.request.ProjectAuthorityRequest;
import com.capstone.domain.project.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomProjectRepositoryImpl implements CustomProjectRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public Project findByProjectName(String projectName) {
        Query query = new Query(Criteria.where("projectName").is(projectName));
        return mongoTemplate.findOne(query, Project.class);
    }

    @Override
    public List<String> getAuthorityKeysByProjectId(String projectId) {
        Query query = new Query(Criteria.where("_id").is(projectId));
        query.fields().include("authorities");
        Project project = mongoTemplate.findOne(query, Project.class);

        if (project == null || project.getAuthorities() == null) {
            throw new IllegalArgumentException("Project not found or authorities field is missing: " + projectId);
        }

        Map<String, String> authorities = project.getAuthorities();
        return new ArrayList<>(authorities.keySet());
    }

    @Transactional
    public void updateAuthority(ProjectAuthorityRequest projectAuthorityRequest) {
        Query query = new Query(Criteria.where("_id").is(projectAuthorityRequest.projectId()));

        Project project = mongoTemplate.findOne(query, Project.class);

        if (project == null) {
            return;
        }

        Map<String, String> existingAuthorities = project.getAuthorities();

        Update update = new Update();
        projectAuthorityRequest.authorities().forEach((email, role) -> {
            if (existingAuthorities.containsKey(email)) {
                String field = "authorities." + email.replace(".", "_");
                update.set(field, role);
            }
        });

        if (!update.getUpdateObject().isEmpty()) {
            mongoTemplate.updateFirst(query, update, Project.class);
        }
    }
}
