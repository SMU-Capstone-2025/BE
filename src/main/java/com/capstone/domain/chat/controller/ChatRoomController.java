package com.capstone.domain.chat.controller;

import com.capstone.domain.chat.dto.ChatRoom;
import com.capstone.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController
{
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAllRooms();
    }

    @GetMapping("/room/{roomId}")
    public ChatRoom getRoom(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        ChatRoom room = chatRoomRepository.createChatRoom(name);
        System.out.println("Created Room: " + room.getRoomId());  // 서버 로그 확인
        return room;
    }
}
