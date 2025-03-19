//package com.capstone.domain.chat.interceptor;
//
//import com.capstone.global.jwt.JwtUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class WebSocketSecurityInterceptor implements ChannelInterceptor {
//
//    private final JwtUtil jwtUtil;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public WebSocketSecurityInterceptor(JwtUtil jwtUtil, @Lazy SimpMessagingTemplate messagingTemplate) {
//        this.jwtUtil = jwtUtil;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel)
//    {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        StompCommand command = accessor.getCommand();
//        String token = accessor.getFirstNativeHeader("Authorization");
//        log.info("token{}", token);
//
//        if (token == null || !token.startsWith("Bearer "))
//        {
//            log.error("JWT 토큰이 없음");
//            sendErrorMessage(accessor, "TOKEN_MISSING");
//            return message;
//        }
//
//        token = token.substring(7);
//
//        if (jwtUtil.isExpired(token))
//        {
//            log.warn("JWT 토큰이 만료됨 - 재인증 요청 필요");
//            sendErrorMessage(accessor, "TOKEN_EXPIRED");
//            return message;
//        }
//
//        //소켓 첫 연결시에만 username 저장
//        if (command == StompCommand.CONNECT)
//        {
//            String username = jwtUtil.getEmail(token);
//            accessor.getSessionAttributes().put("username", username);
//            accessor.addNativeHeader("username", username);
//            log.info("사용자: {}", username);
//
//        }
//
//        return message;
//    }
//    //프론트 단에서 토큰 만료시 재발급 경로로 가서 재발급
//    private void sendErrorMessage(StompHeaderAccessor accessor, String errorType)
//    {
//        String sessionId = accessor.getSessionId();
//        if (sessionId != null) {
//            messagingTemplate.convertAndSend("/queue/errors/" + sessionId, errorType);
//        }
//    }
//}
//
