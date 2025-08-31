package com.capstone.global.kafka.dto.detail;


import com.capstone.domain.document.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChangeDetail extends ChangeDetail<List<String>>{
    private String status;
    private String content;
    private List<String> logs;
    private List<String> attachments;

    public static DocumentChangeDetail from(Document document){
        return DocumentChangeDetail.builder()
                .title(document.getTitle())
                .content(document.getContent())
                .coworkers(document.getEditors())
                .status(document.getStatus())
                .logs(document.getLogs())
                .attachments(document.getAttachments())
                .build();

    }
}
