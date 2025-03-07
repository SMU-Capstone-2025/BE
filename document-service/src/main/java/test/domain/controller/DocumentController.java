package test.domain.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger( DocumentController.class );
    private final SimpMessageSendingOperations messagingTemplate;

    // 클라이언트에서 "/pub/chat/message"로 메시지를 전송하면 실행됨
    @MessageMapping("/chat/message")
    public void sendMessage(Map<String, Object> params,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        String documentId = (String) params.get("documentId");
        String message = (String) params.get("message");

        String email = (String) sessionAttributes.get("email");

        LOGGER.info("메시지 수신 - 사용자: {}, 채널: {}, 내용: {}", email, documentId, message);

        // 사용자 정보를 포함한 메시지 객체 생성
        Map<String, Object> messagePayload = new HashMap<>();
        messagePayload.put("email", email);
        messagePayload.put("message", message);

        // 해당 채널을 구독 중인 모든 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/" + documentId, messagePayload);
    }
}