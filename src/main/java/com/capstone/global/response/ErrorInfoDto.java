package com.capstone.global.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorInfoDto(
        HttpStatus httpStatus,
        String kind,
        String code,
        String message
) {
}
