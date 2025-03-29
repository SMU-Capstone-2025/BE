package com.capstone.domain.project.exception;

import com.capstone.domain.project.message.ProjectMessages;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException() {
        super(ProjectMessages.INVALID_MEMBER);
    }
}
