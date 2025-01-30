package com.capstone.domain.task.util;

import com.capstone.domain.task.message.Status;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.global.util.DateUtil;
import com.capstone.domain.task.dto.TaskDto;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    private final TaskRepository taskRepository;

    public Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .status(Status.IN_PROGRESS)
                .currentVersion(taskDto.getVersion())
                .versionHistory(new ArrayList<>())
                .build();
    }

    public Version createOrGetVersion(TaskDto taskDto, String fileId) {
        Version version = taskRepository.findByTaskIdAndVersion(taskDto.getId(), taskDto.getVersion());
        if (version == null){
            List<String> attachmentList = new ArrayList<>(); // 새로운 리스트 생성
            attachmentList.add(fileId);
            return Version.builder()
                    .taskId(taskDto.getId())
                    .version(taskDto.getVersion())
                    .modifiedDateTime(DateUtil.getCurrentFormattedDateTime())
                    .modifiedBy(taskDto.getModifiedBy())
                    .summary(taskDto.getSummary())
                    .content(taskDto.getContent())
                    .attachmentList(attachmentList)
                    .build();
        }
        version.getAttachmentList().add(fileId);
        return version;
    }

}
