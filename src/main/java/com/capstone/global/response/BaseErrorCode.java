package com.capstone.global.response;

public interface BaseErrorCode {
    ErrorInfoDto getReasonHttpStatus();
    String getMessage();
}
