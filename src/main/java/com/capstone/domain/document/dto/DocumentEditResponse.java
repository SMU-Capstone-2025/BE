package com.capstone.domain.document.dto;

import java.util.List;

public record DocumentEditResponse(
        String documentId,
        DocumentEditVo message,
        List<DocumentCursorDto> cursors
) {

    public static DocumentEditResponse from(DocumentEditRequest request, List<DocumentCursorDto> cursors){
        return new DocumentEditResponse(request.documentId(), request.message(), cursors);
    }
}
