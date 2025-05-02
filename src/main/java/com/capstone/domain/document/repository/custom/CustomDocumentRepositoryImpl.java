package com.capstone.domain.document.repository.custom;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomDocumentRepositoryImpl implements CustomDocumentRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Document findDocumentByDocumentId(String documentId) {
        Query query = new Query(Criteria.where("documentId").is(documentId));
        return mongoTemplate.findOne(query, Document.class);
    }

    @Override
    public List<Document> findDocumentsByProjectId(String projectId) {
        Query query = new Query(Criteria.where("projectId").is(projectId));
        return mongoTemplate.find(query, Document.class);
    }
}
