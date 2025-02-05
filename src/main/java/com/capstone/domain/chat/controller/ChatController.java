package com.capstone.domain.chat.controller;

import com.capstone.domain.chat.dto.ChatRequest;
import com.capstone.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/{chatRoomId}/chat.sendMessage")
    public ChatRequest.ChatMessageDTO sendMessage(@Payload ChatRequest.ChatMessageDTO chatMessage, @DestinationVariable String chatRoomId)
    {
        chatService.processMessage(chatMessage,chatRoomId);
        return chatMessage;
    }

    @MessageMapping("/{chatRoomId}/chat.addUser")
    public ChatRequest.ChatMessageDTO addUser(@Payload ChatRequest.ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String chatRoomId) {
        chatService.enterMessage(chatMessage,headerAccessor,chatRoomId);
        return chatMessage;
    }
    @MessageMapping("/{chatRoomId}/chat.removeUser")
    public ChatRequest.ChatMessageDTO removeUser(@Payload ChatRequest.ChatMessageDTO chatMessage,SimpMessageHeaderAccessor headerAccessor,@DestinationVariable String chatRoomId) {
        chatService.exitMessage(chatMessage,headerAccessor,chatRoomId);
        return chatMessage;
    }

}
