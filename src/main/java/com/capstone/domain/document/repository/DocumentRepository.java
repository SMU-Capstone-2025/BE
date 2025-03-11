package com.capstone.domain.document.repository;

import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.repository.custom.CustomDocumentRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String>, CustomDocumentRepository {
}
