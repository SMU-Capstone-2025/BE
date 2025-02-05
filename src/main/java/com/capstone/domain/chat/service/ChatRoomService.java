package com.capstone.domain.chat.service;

import com.capstone.domain.chat.dto.ChatRoom;
import com.capstone.domain.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService
{
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }
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
