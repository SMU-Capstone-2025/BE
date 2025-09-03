package com.capstone.domain.document.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEditVo {

    private String documentId;

    private DocumentUserDto userDto;

    private String status;

    private String title;

    private String content;

    private Map<String, Long> cursor;

    private List<String> attachments;

}
