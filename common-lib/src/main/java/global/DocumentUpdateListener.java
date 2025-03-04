package global;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Component
public class DocumentUpdateListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String documentUpdate = new String(message.getBody());

        // 해당 문서 ID를 가진 모든 WebSocket 세션에 전송
        // ERROR:  The remote endpoint was in state [TEXT_PARTIAL_WRITING] which is an invalid state for called method
        DocumentWebSocketHandler.sessions.forEach((docId, session) -> {
            try {
                session.sendMessage(new TextMessage(documentUpdate));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}