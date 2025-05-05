package com.capstone.domain.document.controller;

import com.capstone.docs.DocumentControllerDocs;
import com.capstone.domain.document.dto.DocumentCreateRequest;
import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentEditResponse;
import com.capstone.domain.document.dto.DocumentResponse;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.service.DocumentService;
import com.capstone.domain.task.entity.Task;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController implements DocumentControllerDocs {
    private final SimpMessageSendingOperations messagingTemplate;
    private final DocumentService documentService;

    @GetMapping("/load")
    @PreAuthorize("@projectAuthorityEvaluator.hasDocumentPermission(#documentId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@RequestParam("documentId") String documentId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentCacheFirst(documentId)));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@projectAuthorityEvaluator.hasDocumentPermission(#documentId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Document>> deleteDocument(@RequestParam("documentId") String documentId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.deleteDocumentFromCacheAndDB(documentId)));
    }

    @PutMapping("/status")
    @PreAuthorize("@projectAuthorityEvaluator.hasDocumentPermission(#documentId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Document>> putStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String documentId,
            @RequestParam String status){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.updateStatus(documentId, status, userDetails)));
    }

    @PostMapping("/post")
    @PreAuthorize("@projectAuthorityEvaluator.hasPermission(#documentCreateRequest.projectId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Document>> postDocument(@RequestBody DocumentCreateRequest documentCreateRequest){
        System.out.println("called");
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.createDocument(documentCreateRequest)));
    }

    @MessageMapping("/editing")
    public void sendMessage(@Valid DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        DocumentEditResponse documentEditResponse = DocumentEditResponse.from(params);
        messagingTemplate.convertAndSend("/sub/document/" + params.documentId(), documentEditResponse);
        documentService.updateDocumentToCache(params.documentId(), params.message());
    }

    @GetMapping("/load/list")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentList(@RequestParam("projectId") String projectId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentList(projectId)));
    }
}