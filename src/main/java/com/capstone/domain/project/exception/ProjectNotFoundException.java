package com.capstone.domain.project.exception;

import com.capstone.domain.project.message.ProjectMessages;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super(ProjectMessages.PROJECT_NOT_FOUND);
    }
}
