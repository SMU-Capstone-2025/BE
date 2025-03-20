package com.capstone.domain.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class ChatRequest {

    @Data
    @NoArgsConstructor
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
