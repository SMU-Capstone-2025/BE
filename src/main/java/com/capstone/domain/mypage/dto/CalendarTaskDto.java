package com.capstone.domain.mypage.dto;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;

import java.time.LocalDate;

public record CalendarTaskDto(
        String id,
        String title,
        String status,
        LocalDate deadline
) {
    public static CalendarTaskDto from(Task task) {

        return new CalendarTaskDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getDeadline()
        );
    }
}