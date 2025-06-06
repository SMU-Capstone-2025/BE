package com.capstone.global.response.exception;

import com.capstone.global.response.BaseErrorCode;
import com.capstone.global.response.ErrorInfoDto;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseErrorCode code;

    public GlobalException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorInfoDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }


}