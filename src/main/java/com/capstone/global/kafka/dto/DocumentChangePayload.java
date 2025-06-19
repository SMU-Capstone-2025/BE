package com.capstone.global.kafka.dto;

import com.capstone.domain.document.entity.Document;
import com.capstone.global.kafka.dto.detail.DocumentChangeDetail;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChangePayload extends CommonChangePayload{
    private DocumentChangeDetail oldContent;
    private DocumentChangeDetail newContent;

    public static DocumentChangePayload from(Document document, DocumentChangeDetail oldContent,
                                             DocumentChangeDetail newContent, String email,
                                             List<String> coworkers){
        return DocumentChangePayload.builder()
                .id(document.getId())
                .title(document.getTitle())
                .modifiedBy(email)
                .oldContent(oldContent)
                .newContent(newContent)
                .coworkers(coworkers)
                .build();
    }

}
