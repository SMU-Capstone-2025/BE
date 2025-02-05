package com.capstone.domain.chat.controller;

import com.capstone.domain.chat.dto.ChatRequest;
import com.capstone.domain.chat.dto.ChatRoom;
import com.capstone.domain.chat.repository.ChatRoomRepository;
import com.capstone.domain.chat.service.ChatRoomService;
import com.capstone.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController
{
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @GetMapping("/rooms")
    public List<ChatRoom> getAllRoomsService()
    {
        return chatRoomService.getRooms();
    }

    @GetMapping("/room/{roomId}")
    public ChatRoom getRoomService(@PathVariable String roomId)
    {
        return chatRoomService.getRoom(roomId);
    }

    @PostMapping("/room")
    public ChatRoom createRoomService(@RequestParam String name)
    {
        ChatRoom room = chatRoomService.createRoom(name);
        System.out.println("Created Room: " + room.getRoomId());  // 서버 로그 확인
        return room;
    }
    @GetMapping("/room/{roomId}/history")
    public List<ChatRequest.ChatMessageDTO> getChatHistory(@PathVariable String roomId)
    {
        return chatService.getChatMessages(roomId,50);
    }
}
