package com.capstone.domain.task.util;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.util.DateTimeUtil;
import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.entity.Version;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    private final TaskRepository taskRepository;

    public Version createOrGetVersion(TaskRequest taskDto, String fileId) {
        Task task = taskRepository.findById(taskDto.taskId()).orElseThrow();
        try {
            Version version = VersionUtil.getCurrentVersionEntity(task);
            version.getAttachmentList().add(fileId);
            return version;
        } catch (GlobalException e) {
            List<String> attachmentList = new ArrayList<>();
            attachmentList.add(fileId);
            return Version.builder()
                    .taskId(taskDto.taskId())
                    .version(taskDto.version())
                    .modifiedDateTime(DateTimeUtil.getCurrentFormattedDateTime())
                    .modifiedBy(taskDto.modifiedBy())
                    .content(taskDto.content())
                    .attachmentList(attachmentList)
                    .build();

        }
    }

}
