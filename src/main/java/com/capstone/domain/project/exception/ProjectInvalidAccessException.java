package com.capstone.domain.project.exception;

import com.capstone.domain.project.message.ProjectMessages;

public class ProjectInvalidAccessException extends RuntimeException {
    public ProjectInvalidAccessException() {
        super(ProjectMessages.PROJECT_NOT_ACCESS);
    }
}
