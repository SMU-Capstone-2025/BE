package com.capstone.domain.log.repository.custom;

import com.capstone.domain.document.dto.DocumentLogDto;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.log.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomLogRepositoryImpl implements CustomLogRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public List<LogEntity> findAllByTaskId(String taskId) {
       Query query = new Query(Criteria.where("taskId").is(taskId));
       return mongoTemplate.find(query, LogEntity.class);
    }

    @Override
    public Page<DocumentLogDto> findAllByDocumentId(String documentId, Pageable pageable) {
        Query base = new Query(
                Criteria.where("targetId").is(documentId)
                        .and("targetType").is("DOCUMENT")
        );

        long total = mongoTemplate.count(base, "logs");

        Query pageQuery = base.with(pageable)
                .with(Sort.by(Sort.Direction.DESC, "createdAt"));

        pageQuery.fields()
                .include("email")
                .include("createdAt")
                .include("oldContent").include("newContent");

        List<DocumentLogDto> content =
                mongoTemplate.find(pageQuery, DocumentLogDto.class, "logs");

        return new PageImpl<>(content, pageable, total);
    }

}
