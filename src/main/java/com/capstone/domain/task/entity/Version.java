package com.capstone.domain.task.entity;

import com.capstone.global.entity.BaseDocument;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version extends BaseDocument{
    private String taskId;
    private String version;
    private String modifiedDateTime;
    private String modifiedBy;
    private String content;
    private List<Attachment> attachmentList;

    public void addAttachment(String fileId,String fileName) {
        if (attachmentList == null) {
            attachmentList = new ArrayList<>();
        }
        attachmentList.add(new Attachment(fileId, fileName));
    }
}

