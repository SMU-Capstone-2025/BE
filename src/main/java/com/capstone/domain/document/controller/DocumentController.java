package com.capstone.domain.document.controller;

import com.capstone.docs.DocumentControllerDocs;
import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentEditResponse;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.service.DocumentService;
import com.capstone.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController implements DocumentControllerDocs {
    private final SimpMessageSendingOperations messagingTemplate;
    private final DocumentService documentService;

    @GetMapping("/load")
    public ResponseEntity<ApiResponse<Document>> getDocument(@RequestParam("documentId") String documentId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentCacheFirst(documentId)));
    }

    @MessageMapping("/editing")
    public void sendMessage(@Valid DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        DocumentEditResponse documentEditResponse = DocumentEditResponse.from(params);
        messagingTemplate.convertAndSend("/sub/document/" + params.documentId(), documentEditResponse);
        documentService.updateDocumentToCache(params.documentId(), params.message());
    }
}