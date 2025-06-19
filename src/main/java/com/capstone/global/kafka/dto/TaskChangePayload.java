package com.capstone.global.kafka.dto;

import com.capstone.domain.task.entity.Task;
import com.capstone.global.kafka.dto.detail.TaskChangeDetail;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TaskChangePayload extends CommonChangePayload{
    private TaskChangeDetail oldContent;
    private TaskChangeDetail newContent;

    public static TaskChangePayload from(Task task, TaskChangeDetail oldContent,
                                         TaskChangeDetail newContent, String email,
                                         List<String> coworkers) {
        return TaskChangePayload.builder()
                .id(task.getId())
                .title(task.getTitle())
                .modifiedBy(email)
                .oldContent(oldContent)
                .newContent(newContent)
                .coworkers(coworkers)
                .build();
    }
}