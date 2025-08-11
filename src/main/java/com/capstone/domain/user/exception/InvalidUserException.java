package com.capstone.domain.user.exception;

import com.capstone.domain.user.message.UserMessages;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

public class InvalidUserException extends GlobalException {
    public InvalidUserException(Object detail) {
        super(ErrorStatus.USER_NOT_FOUND, detail);
    }
}
