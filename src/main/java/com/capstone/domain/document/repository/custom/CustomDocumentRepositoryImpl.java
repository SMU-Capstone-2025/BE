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
    public List<Document> findDocumentsByDocumentList(List<String> documentIds) {
        Query query = new Query(Criteria.where("_id").in(documentIds));
        return mongoTemplate.find(query, Document.class);
    }

}
