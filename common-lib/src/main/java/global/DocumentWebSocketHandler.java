package global;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DocumentWebSocketHandler implements WebSocketHandler {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    // 연결된 세션 관리 (문서 ID 기준)
    public static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String documentId = "d";//getDocumentIdFromSession(session);
        sessions.put(documentId, session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String documentId = "d";//getDocumentIdFromSession(session);
        String payload = (String) message.getPayload();
        System.out.println(payload);
        System.out.println(documentId);
        // Redis Pub/Sub을 통해 변경사항 브로드캐스트
        redisTemplate.convertAndSend("document-updates", payload);

        // 같은 문서를 보고 있는 모든 사용자에게 전송
        broadcastUpdate(documentId, payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String documentId =  "d";//getDocumentIdFromSession(session);
        sessions.remove(documentId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void broadcastUpdate(String documentId, String message) {
        System.out.println("id: " + documentId);
        sessions.forEach((id, session) -> {
            if (id.equals(documentId)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



//    private String getDocumentIdFromSession(WebSocketSession session) {
//        return session.getUri().getQuery().split("=")[1]; // ws://localhost/ws/document?docId=1234
//    }
}