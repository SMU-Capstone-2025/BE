package com.capstone.domain.document.interceptor;

import com.capstone.global.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = headerAccessor.getCommand();

        if (command != null && command != StompCommand.DISCONNECT)
        {
            List<String> authHeaders = headerAccessor.getNativeHeader("Authorization");
            String authorizationHeader = (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0) : null;

            if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
                throw new MessageDeliveryException("토큰이 없거나 유효하지 않습니다.");
            }

            String token = authorizationHeader.substring(BEARER_PREFIX.length());

            if (jwtUtil.isExpired(token)) {
                throw new JwtException("토큰이 만료되었습니다.");
            }

            headerAccessor.getSessionAttributes().put("email", jwtUtil.getEmail(token));

        }

        return message;
    }
}
