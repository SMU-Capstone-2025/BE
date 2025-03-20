package com.capstone.domain.chat.repository;


import com.capstone.domain.chat.dto.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChatRoomRepository
{
    private final Map<String, ChatRoom> chatRooms=new HashMap<>();

    public List<ChatRoom> findAllRooms() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

}