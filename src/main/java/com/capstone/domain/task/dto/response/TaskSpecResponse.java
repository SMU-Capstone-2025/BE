package com.capstone.domain.task.dto.response;

import com.capstone.domain.task.entity.Attachment;
import com.capstone.domain.task.entity.Task;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record  TaskSpecResponse(
        String taskId,
        String title,
        String status,
        String content,
        LocalDate deadline,
        List<AttachmentDto> attachmentList,
        List<String> coworkers
) {
    public static TaskSpecResponse from(Task task, List<AttachmentDto> attachments,String content){
        return TaskSpecResponse.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .coworkers(task.getEditors())
                .attachmentList(attachments)
                .content(content)
                .build();
    }
}
