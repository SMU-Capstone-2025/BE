package com.capstone.domain.document.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/editing")
    public void sendMessage(Map<String, Object> params) {
        String documentId = (String) params.get("documentId");
        String message = (String) params.get("message");

        messagingTemplate.convertAndSend("/sub/chat/" + documentId, message);
    }
}