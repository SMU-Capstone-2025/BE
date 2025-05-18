package com.capstone.domain.log.repository.custom;

import com.capstone.domain.log.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomLogRepositoryImpl implements CustomLogRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<LogEntity> findAllByTaskId(String taskId) {
       Query query = new Query(Criteria.where("taskId").is(taskId));
       return mongoTemplate.find(query, LogEntity.class);
    }



}
