package com.capstone.domain.task.util;

import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.global.util.DateUtil;
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
        Version version = taskRepository.findByTaskIdAndVersion(taskDto.taskId(), taskDto.version());
        if (version == null){
            List<String> attachmentList = new ArrayList<>(); // 새로운 리스트 생성
            attachmentList.add(fileId);
            return Version.builder()
                    .taskId(taskDto.taskId())
                    .version(taskDto.version())
                    .modifiedDateTime(DateUtil.getCurrentFormattedDateTime())
                    .modifiedBy(taskDto.modifiedBy())
                    .content(taskDto.content())
                    .attachmentList(attachmentList)
                    .build();
        }
        version.getAttachmentList().add(fileId);
        return version;
    }

}
