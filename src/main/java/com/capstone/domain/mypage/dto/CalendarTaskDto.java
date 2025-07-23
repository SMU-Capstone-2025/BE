package com.capstone.domain.mypage.dto;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CalendarTaskDto(
        String id,
        String title,
        String status,
        LocalDate end,
        LocalDateTime start
) {
    public static CalendarTaskDto from(Task task) {

        return new CalendarTaskDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getDeadline(),
                task.getCreatedAt()
        );
    }
}