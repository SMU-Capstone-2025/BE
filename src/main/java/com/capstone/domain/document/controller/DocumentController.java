package com.capstone.domain.document.controller;

import com.capstone.docs.DocumentControllerDocs;
import com.capstone.domain.document.dto.*;
import com.capstone.domain.document.entity.Document;
import com.capstone.domain.document.service.DocumentService;
import com.capstone.global.response.ApiResponse;
import com.capstone.global.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController implements DocumentControllerDocs {
    private final SimpMessageSendingOperations messagingTemplate;
    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @GetMapping("/load")
    @PreAuthorize("@projectAuthorityEvaluator.hasDocumentPermission(#documentId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@RequestParam("documentId") String documentId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentCacheFirst(documentId)));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@projectAuthorityEvaluator.hasDocumentPermission(#documentId, {'ROLE_MANAGER','ROLE_MEMBER'}, authentication)")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("documentId") String documentId){
        documentService.deleteDocumentFromCacheAndDB(customUserDetails, documentId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
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
    public ResponseEntity<ApiResponse<Void>> postDocument(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DocumentCreateRequest documentCreateRequest){
        documentService.createDocument(customUserDetails, documentCreateRequest);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @MessageMapping("/editing")
    public void sendMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                            @Valid DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        try {
            String email = (String) sessionAttributes.get("email");

            log.info("params:{}", params.message().getContent());
            DocumentEditVo documentEditVo = params.message();
            log.info("documentEditVo:{}", documentEditVo.getContent());
            log.info("user:{}", documentEditVo.getUser());

            documentService.updateDocumentEditStatus(documentEditVo);

            List<DocumentCursorDto> otherCursors = documentService.findOtherUsersCursor(documentEditVo.getDocumentId());
            for (DocumentCursorDto otherCursor : otherCursors) {
                log.info("cursorUser:{}", otherCursor.getUserName());
            }


            DocumentEditResponse documentEditResponse = DocumentEditResponse.from(params, otherCursors);
            messagingTemplate.convertAndSend("/sub/document/" + params.documentId(), documentEditResponse);
            documentService.updateDocumentToCache(email, params.documentId(), documentEditVo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/load/list")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentList(@RequestParam("projectId") String projectId){
        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentList(projectId)));
    }

    @GetMapping("/load/list/date-asc")
    public ResponseEntity<ApiResponse<List<Document>>> getDocumentListSortedByCreateAt(
            @RequestParam("projectId") String projectId) {

        return ResponseEntity.ok(ApiResponse.onSuccess(documentService.findDocumentListSortedByDateAsc(projectId)));
    }


}