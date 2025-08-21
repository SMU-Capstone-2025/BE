package com.capstone.domain.document.dto;

import com.capstone.domain.document.entity.Document;

public record DocumentWrapper(
        Document document,
        String editor
) {
    public static DocumentWrapper toDto(Document document, String editor){
        return new DocumentWrapper(document, editor);
    }
}
