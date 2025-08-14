package com.capstone.domain.task.dto.response;

import com.capstone.domain.task.entity.Attachment;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TaskVersionResponse(
        String taskId,
        String version,
        String modifiedDateTime,
        String modifiedBy,
        String content,
        List<Attachment> attachmentList,
        String title,
        LocalDate deadline,
        List<String> editors

) {
    public static TaskVersionResponse from(Version version,String taskId,String title, LocalDate deadline,List<String> editors) {
        return TaskVersionResponse.builder()
                .taskId(taskId)
                .version(version.getVersion())
                .modifiedDateTime(version.getModifiedDateTime())
                .modifiedBy(version.getModifiedBy())
                .content(version.getContent())
                .attachmentList(version.getAttachmentList())
                .title(title)
                .deadline(deadline)
                .editors(editors)
                .build();
    }
}

