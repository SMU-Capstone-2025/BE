package com.capstone.domain.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
