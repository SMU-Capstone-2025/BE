package com.capstone.global.kafka.dto;

import com.capstone.domain.task.entity.Task;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChangePayload extends CommonChangePayload{
    private String id;
    private String title;
    private String modifiedBy;
    private String version;
    private String oldContent;
    private String newContent;

    public static TaskChangePayload from(Task task, String oldContent, String newContent, String email) {
        return TaskChangePayload.builder()
                .id(task.getId())
                .title(task.getTitle())
                .modifiedBy(email)
                .version(task.getCurrentVersion())
                .oldContent(oldContent)
                .newContent(newContent)
                .build();
    }
}