package com.capstone.domain.chat.service;

import com.capstone.domain.chat.dto.ChatRequest;
import com.capstone.domain.chat.exception.ChatNotFoundException;
import com.capstone.domain.chat.exception.MessageProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, ChatRequest.ChatMessageDTO> redisTemplate;
    private final ObjectMapper objectMapper;

    public void processMessage(ChatRequest.ChatMessageDTO message, String chatRoomId)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }

        saveChatMessage(chatRoomId, message);

        String userMessage = message.getContent();
        System.out.println("Processing message: " + userMessage + " Room ID: " + chatRoomId);

        redisTemplate.convertAndSend("chatRoom:" + chatRoomId, message);
    }

    public void enterMessage(ChatRequest.ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor, String chatRoomId)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        message.setType(ChatRequest.ChatMessageDTO.MessageType.JOIN);
        message.setContent(message.getSender()+"님이 입장하였습니다.");

        redisTemplate.convertAndSend("chatRoom:" +chatRoomId, message);
    }
    public void exitMessage(ChatRequest.ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor, String chatRoomId)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }

        headerAccessor.getSessionAttributes().put("username", message.getSender());

        message.setType(ChatRequest.ChatMessageDTO.MessageType.LEAVE);
        message.setContent(message.getSender()+"님이 퇴장하였습니다.");
        redisTemplate.convertAndSend("chatRoom:" +chatRoomId, message);
    }

    public void saveChatMessage(String roomId, ChatRequest.ChatMessageDTO message)
    {
        String key = "chat:room:" + roomId;
        redisTemplate.opsForList().leftPush(key, message);
        redisTemplate.opsForList().trim(key, 0, 49);
        redisTemplate.expire(key, Duration.ofHours(1));
    }
    public List<ChatRequest.ChatMessageDTO> getChatMessages(String roomId, int limit)
    {
        String key = "chat:room:" + roomId;
        List<ChatRequest.ChatMessageDTO> messages = redisTemplate.opsForList().range(key, 0, limit - 1);
        return messages;
    }





}
