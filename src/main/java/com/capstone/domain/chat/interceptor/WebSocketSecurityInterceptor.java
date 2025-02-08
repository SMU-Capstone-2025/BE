package com.capstone.domain.chat.interceptor;

import com.capstone.global.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSecurityInterceptor(JwtUtil jwtUtil, SimpMessagingTemplate messagingTemplate) {
        this.jwtUtil = jwtUtil;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("token{}", token);

            if (token != null) {
                try {
                    if (jwtUtil.isExpired(token)) {
                        log.warn("JWT 토큰이 만료 되었습니다 - 재인증 요청 보내야합니다.");
                        messagingTemplate.convertAndSendToUser(
                                accessor.getSessionId(),
                                "/queue/errors",
                                "TOKEN_EXPIRED"
                        );
                        //이후에 프론트에서 웹소켓 재연결
                        throw new IllegalArgumentException("JWT 토큰이 만료되었습니다.");
                    }

                    String name = jwtUtil.getName(token);
                    accessor.getSessionAttributes().put("username",name);
                    log.info(name);
                } catch (Exception e) {
                    log.error("JWT 검증 실패: {}", e.getMessage());
                    throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
                }
            } else {
                log.error("JWT 토큰이 없음");
                throw new IllegalArgumentException("JWT 토큰이 필요합니다.");
            }
        }

        return message;
    }
}

