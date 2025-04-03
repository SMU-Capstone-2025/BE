package com.capstone.domain.document.controller;

import com.capstone.docs.DocumentControllerDocs;
import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentEditResponse;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
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
    public Document getDocument(@RequestParam("documentId") String documentId){
        return documentService.findDocumentCacheFirst(documentId);
    }

    @MessageMapping("/editing")
    public void sendMessage(DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        DocumentEditResponse documentEditResponse = new DocumentEditResponse((String) sessionAttributes.get("email"), params.getMessage());
        messagingTemplate.convertAndSend("/sub/document/" + params.getDocumentId(), documentEditResponse);
        documentService.updateDocumentToCache(params.getDocumentId(), params.getMessage());
    }
}