package com.capstone.domain.document.repository.custom;

import com.capstone.domain.document.entity.Document;

import java.util.List;

public interface CustomDocumentRepository {
    Document findDocumentByDocumentId(String documentId);
    List<Document> findDocumentsByProjectId(String projectId);
}
