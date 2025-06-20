package com.capstone.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class CommonChangePayload {
    private String id;
    private String title;
    private String modifiedBy;
    private List<String> coworkers;
}
