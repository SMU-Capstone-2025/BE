package com.capstone.global.elastic.repository;

import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.elastic.repository.custom.CustomLogRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<LogEntity, String>, CustomLogRepository {
}