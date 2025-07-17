package com.capstone.domain.task.util;

import com.capstone.domain.file.dto.FileResponse;
import com.capstone.domain.task.entity.Attachment;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.util.DateTimeUtil;
import com.capstone.domain.task.dto.request.TaskRequest;
import com.capstone.domain.task.entity.Version;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskUtil {
    private final TaskRepository taskRepository;

    public Version createOrGetVersion(TaskRequest taskDto,String fileId, String fileName) {
        Task task = taskRepository.findById(taskDto.taskId()).orElseThrow();

        try {
            Version current = VersionUtil.getCurrentVersionEntity(task);
            List<Attachment> attachmentList = new ArrayList<>();
            if (current.getAttachmentList() != null) {
                attachmentList.addAll(current.getAttachmentList());
            }

            if(fileId!=null && fileName!=null){
                attachmentList.add(new Attachment(fileId,fileName));
            }


            return Version.builder()
                    .taskId(taskDto.taskId())
                    .version(taskDto.version())
                    .modifiedDateTime(DateTimeUtil.getCurrentFormattedDateTime())
                    .modifiedBy(taskDto.modifiedBy())
                    .content(taskDto.content())
                    .attachmentList(attachmentList)
                    .build();

        } catch (GlobalException e) {
            List<Attachment> attachmentList = new ArrayList<>();

            if(fileId!=null && fileName!=null){
                attachmentList.add(new Attachment(fileId,fileName));
            }
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
