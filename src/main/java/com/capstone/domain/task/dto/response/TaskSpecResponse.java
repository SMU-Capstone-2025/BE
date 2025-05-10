package com.capstone.domain.task.dto.response;

import com.capstone.domain.task.entity.Task;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record TaskSpecResponse(
        String task_id,
        String title,
        String status,
        String summary,
        String deadline,
        List<String> attachments,
        List<String> coworkers
) {
    public static TaskSpecResponse from(Task task, String summary, List<String> attachments){
        return TaskSpecResponse.builder()
                .task_id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .summary(summary)
                .deadline(task.getDeadline().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")).toString())
                .coworkers(task.getEditors())
                .attachments(attachments)
                .build();
    }
}
