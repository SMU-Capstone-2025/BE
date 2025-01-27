package com.capstone.global.elastic.repository;

import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.elastic.repository.custom.CustomLogRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LogRepository extends ElasticsearchRepository<LogEntity, String>, CustomLogRepository {
}