package com.capstone.domain.chat.exception;

public class MessageProcessingException extends RuntimeException {
    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
