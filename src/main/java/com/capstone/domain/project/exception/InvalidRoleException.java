package com.capstone.domain.project.exception;

import com.capstone.domain.project.message.ProjectMessages;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
        super(ProjectMessages.INVALID_ROLE);
    }
}
