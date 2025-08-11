package com.capstone.global.response.exception;

import com.capstone.global.response.BaseErrorCode;
import com.capstone.global.response.ErrorInfoDto;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseErrorCode code;
    private final Object detail;

    public GlobalException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
        this.detail = null;
    }

    public GlobalException(BaseErrorCode errorStatus, Object detail) {
        super(errorStatus.getMessage());
        this.code = errorStatus;
        this.detail = detail;
    }


    public ErrorInfoDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }


}