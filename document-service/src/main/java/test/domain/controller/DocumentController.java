package test.domain.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import test.domain.dto.DocumentEditRequest;
import test.domain.dto.DocumentEditResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger( DocumentController.class );
    private final SimpMessageSendingOperations messagingTemplate;

    // 클라이언트에서 "/pub/chat/message"로 메시지를 전송하면 실행됨
    @MessageMapping("/chat/message")
    public void sendMessage(DocumentEditRequest params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        DocumentEditResponse documentEditResponse=  new DocumentEditResponse((String) sessionAttributes.get("email"), params.getMessage());
        messagingTemplate.convertAndSend("/sub/chat/" + params.getDocumentId(), documentEditResponse);
    }
}