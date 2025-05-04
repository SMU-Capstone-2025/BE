package com.capstone.domain.document.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentStatus {
    PENDING("진행 전"),
    PROGRESS("진행 중"),
    COMPLETED("진행 완료"),
    ETC("기타 문서");


    private final String value;

    public static boolean isValid(String value) {
        try {
            DocumentStatus.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
