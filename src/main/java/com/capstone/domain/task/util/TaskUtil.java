package com.capstone.domain.task.util;

import com.capstone.domain.task.message.Status;
import com.capstone.global.util.DateUtil;
import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;

import java.util.ArrayList;

public class TaskUtil {
    public static Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .status(Status.IN_PROGRESS)
                .currentVersion(taskDto.getVersion())
                .versionHistory(new ArrayList<>())
                .build();
    }

    public static Version createVersion(TaskDto taskDto) {
        return Version.builder()
                .taskId(taskDto.getId())
                .version(taskDto.getVersion())
                .modifiedDateTime(DateUtil.getCurrentFormattedDateTime())
                .modifiedBy(taskDto.getModifiedBy())
                .summary(taskDto.getSummary())
                .content(taskDto.getContent())
                .build();
    }

}
