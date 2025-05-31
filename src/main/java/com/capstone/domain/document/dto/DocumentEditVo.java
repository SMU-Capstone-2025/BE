package com.capstone.domain.document.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEditVo {

    private String documentId;

    private String status;

    private String title;

    private String content;

    private List<String> logs;

    private List<String> attachments;

}
