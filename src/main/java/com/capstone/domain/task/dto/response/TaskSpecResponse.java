package com.capstone.domain.task.dto.response;

import com.capstone.domain.task.entity.Task;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record TaskSpecResponse(
        String taskId,
        String title,
        String status,
        String content,
        LocalDate deadline,
        List<String> attachments,
        List<String> coworkers
) {
    public static TaskSpecResponse from(Task task, List<String> attachments,String content){
        return TaskSpecResponse.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .coworkers(task.getEditors())
                .attachments(attachments)
                .content(content)
                .build();
    }
}
