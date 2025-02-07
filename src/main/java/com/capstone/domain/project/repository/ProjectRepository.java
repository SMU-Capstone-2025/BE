package com.capstone.domain.project.repository;

import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.repository.custom.CustomProjectRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String>, CustomProjectRepository {
}
