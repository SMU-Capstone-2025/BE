package com.capstone.domain.user.exception;

import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

public class ProjectUserFoundException extends GlobalException {
    public ProjectUserFoundException() {
        super(ErrorStatus.ALREADY_EXIST_MEMBER);
    }
}
