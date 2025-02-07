package com.capstone.domain.project.repository.custom;

import com.capstone.domain.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CustomProjectRepositoryImpl implements CustomProjectRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public Project findByProjectName(String projectName) {
        Query query = new Query(Criteria.where("projectName").is(projectName));
        return mongoTemplate.findOne(query, Project.class);
    }

    @Override
    public List<String> getAuthorityKeysByProjectId(String projectId) {
        System.out.println(projectId);
        Query query = new Query(Criteria.where("_id").is(projectId));
        query.fields().include("authorities"); // ✅ authorities 필드만 가져오기

        Project project = mongoTemplate.findOne(query, Project.class);

        if (project == null || project.getAuthorities() == null) {
            throw new IllegalArgumentException("Project not found or authorities field is missing: " + projectId);
        }

        Map<String, String> authorities = project.getAuthorities();
        return new ArrayList<>(authorities.keySet());
    }
}
