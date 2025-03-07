package com.capstone.domain.document.controller;

import com.capstone.domain.document.dto.DocumentEditRequest;
import com.capstone.domain.document.dto.DocumentEditResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/editing")
    public void sendMessage(DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        DocumentEditResponse documentEditResponse=  new DocumentEditResponse((String) sessionAttributes.get("email"), params.getMessage());
        messagingTemplate.convertAndSend("/sub/chat/" + params.getDocumentId(), documentEditResponse);
    }
}