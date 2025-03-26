package com.capstone.domain.chat.service;

import com.capstone.domain.chat.dto.ChatRequest;
import com.capstone.domain.chat.exception.ChatNotFoundException;
import com.capstone.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, ChatRequest.ChatMessageDTO> redisTemplate;


    public ChatRequest.ChatMessageDTO processMessage(ChatRequest.ChatMessageDTO message, String chatRoomId,SimpMessageHeaderAccessor headerAccessor)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }

        String userMessage = message.getContent();
        System.out.println("Processing message: " + userMessage + " Room ID: " + chatRoomId);

        String name = (String) headerAccessor.getSessionAttributes().get("username");
        // 채팅 50개만 Redis에  저장 -> 채팅방 나갔다 들어와도 저장 되어있음
        saveChatMessage(chatRoomId, message,name);

        message.setSender(name);
        System.out.println("Message saved to: " + message.getContent());
        redisTemplate.convertAndSend("chatRoom:" + chatRoomId, message);
        return message;
    }

    public void enterMessage(ChatRequest.ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor, String chatRoomId)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }

        //인터셉터에서 이미 이름 헤더에 저장
        String name=headerAccessor.getSessionAttributes().get("username").toString();
        message.setSender(name);

        message.setContent(message.getSender()+"님이 입장하였습니다.");

        redisTemplate.convertAndSend("chatRoom:" +chatRoomId, message);
    }

    public void exitMessage(ChatRequest.ChatMessageDTO message, SimpMessageHeaderAccessor headerAccessor, String chatRoomId)
    {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new ChatNotFoundException("채팅방을 찾을 수 없습니다.");
        }

        String name=headerAccessor.getSessionAttributes().put("username", message.getSender()).toString();

        message.setSender(name);

        message.setContent(message.getSender()+"님이 퇴장하였습니다.");
        redisTemplate.convertAndSend("chatRoom:" +chatRoomId, message);
    }

    public void saveChatMessage(String roomId, ChatRequest.ChatMessageDTO message,String username)
    {
        String key = "chat:room:" + roomId;
        message.setSender(username);
        redisTemplate.opsForList().rightPush(key, message);
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
