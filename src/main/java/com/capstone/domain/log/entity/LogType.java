package com.capstone.domain.log.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogType {
    DOCUMENT("DOCUMENT"), PROJECT("PROJECT"), TASK("TASK");

    private final String type;
}
