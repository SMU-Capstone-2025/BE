package com.capstone.task.util;

import com.capstone.task.dto.TaskDto;
import com.capstone.task.entity.Task;
import com.capstone.task.entity.Version;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskUtil {
    public static Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .currentVersion(taskDto.getVersion())
                .versionHistory(new ArrayList<>())
                .build();
    }

    public static Version createVersion(TaskDto taskDto) {
        return Version.builder()
                .version(taskDto.getVersion())
                .modifiedDateTime(formattedDateTime)
                .modifiedBy(taskDto.getModifiedBy())
                .summary(taskDto.getSummary())
                .content(taskDto.getContent())
                .build();
    }

}
