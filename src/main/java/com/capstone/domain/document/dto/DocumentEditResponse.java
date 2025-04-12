package com.capstone.domain.document.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public record DocumentEditResponse(
        String email,
        String message
) {

    public static DocumentEditResponse from(DocumentEditRequest request){
        return new DocumentEditResponse(request.documentId(), request.message());
    }
}
