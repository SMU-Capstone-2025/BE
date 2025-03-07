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


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("preSend: {}", message.getPayload());
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // 헤더 토큰 얻기
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        // 토큰 자르기 fixme 토큰 자르는 로직 validate 로 리팩토링

        if(authorizationHeader == null || authorizationHeader.equals("null")){
            throw new MessageDeliveryException("메세지 예외");
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        if (jwtUtil.isExpired(token)){
            throw new JwtException(token);
        }

        String email = jwtUtil.getEmail(token);
        headerAccessor.getSessionAttributes().put("email", email);

        return message;

    }
}
