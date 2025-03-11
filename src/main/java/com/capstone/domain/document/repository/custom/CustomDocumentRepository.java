package com.capstone.domain.document.repository.custom;

import com.capstone.domain.document.entity.Document;

public interface CustomDocumentRepository {
    Document findDocumentByDocumentId(String documentId);
}
