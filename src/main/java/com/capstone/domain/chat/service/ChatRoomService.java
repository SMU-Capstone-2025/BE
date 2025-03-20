package com.capstone.domain.chat.service;

import com.capstone.domain.chat.dto.ChatRoom;
import com.capstone.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService
{
    private final ChatRoomRepository chatRoomRepository;

    
    public List<ChatRoom> getRooms()
    {
        return chatRoomRepository.findAllRooms();
    }
    public ChatRoom getRoom(String roomId)
    {
        return chatRoomRepository.findRoomById(roomId);
    }

    public ChatRoom createRoom (String roomName)
    {
        return  chatRoomRepository.createChatRoom(roomName);
    }
}
