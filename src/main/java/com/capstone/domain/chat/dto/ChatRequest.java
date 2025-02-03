package com.capstone.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

public class ChatRequest {

    @Getter
    @Setter
    public static class ChatMessageDTO {
        public enum MessageType{
            CHAT,JOIN,LEAVE
        }
        private MessageType type;

        private String content;

        private String sender;

        private String roomId;

    }

}
