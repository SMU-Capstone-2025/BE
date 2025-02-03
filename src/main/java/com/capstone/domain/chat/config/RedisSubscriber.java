package com.capstone.domain.chat.config;

import com.capstone.domain.chat.dto.ChatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component // Spring Bean 등록
public class RedisSubscriber implements MessageListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatRequest.ChatMessageDTO chatMessage = objectMapper.readValue(jsonMessage, ChatRequest.ChatMessageDTO.class);

            log.info("Redis 메시지 수신 채팅방: {} | 내용: {}", chatMessage.getRoomId(), chatMessage.getContent());

            messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Redis 메시지 변환 오류", e);
        }
    }


}

