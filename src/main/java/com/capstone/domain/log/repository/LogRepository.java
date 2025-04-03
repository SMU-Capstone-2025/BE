package com.capstone.domain.log.repository;

import com.capstone.domain.log.entity.LogEntity;
import com.capstone.domain.log.repository.custom.CustomLogRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<LogEntity, String>, CustomLogRepository {
}