package com.capstone.domain.user.exception;

import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;

public class UserNotFoundException extends GlobalException {
  public UserNotFoundException() {
    super(ErrorStatus.USER_NOT_FOUND);
  }
}
