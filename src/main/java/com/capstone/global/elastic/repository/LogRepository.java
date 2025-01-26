package com.capstone.global.elastic.repository;

import com.capstone.global.elastic.entity.LogEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LogRepository extends ElasticsearchRepository<LogEntity, String> {
}