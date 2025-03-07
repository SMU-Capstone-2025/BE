package test.domain.interceptor;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import test.global.jwt.JwtUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("[AuthInterceptor] WebSocket CONNECT 메시지 수신: {}", message.getPayload());
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // 1. `Authorization` 헤더 가져오기 (List 형태)
        List<String> authHeaders = headerAccessor.getNativeHeader("Authorization");
        String authorizationHeader = (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0) : null;

        // 2. 헤더가 없거나 잘못된 경우 예외 발생
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.error("[AuthInterceptor] Authorization 헤더가 누락되었거나 잘못된 형식입니다.");
            throw new MessageDeliveryException("토큰이 없거나 유효하지 않습니다.");
        }

        // 3. Bearer 토큰 추출
        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        // 4. JWT 유효성 검사
        if (jwtUtil.isExpired(token)) {
            log.error("[AuthInterceptor] 토큰이 만료되었습니다: {}", token);
            throw new JwtException("토큰이 만료되었습니다.");
        }

        // 5. 토큰에서 이메일 추출
        String email = jwtUtil.getEmail(token);
        log.info("[AuthInterceptor] 사용자 이메일 인증 완료: {}", email);

        // 6. 세션 속성에 사용자 이메일 저장
        headerAccessor.getSessionAttributes().put("email", email);

        return message;
    }
}
