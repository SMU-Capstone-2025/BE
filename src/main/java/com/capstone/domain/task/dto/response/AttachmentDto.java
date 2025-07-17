package com.capstone.domain.task.dto.response;

import lombok.Builder;

@Builder
public record AttachmentDto (
        String fileId,
        String fileName
)
{
    public static AttachmentDto from(String fileId,String fileName)
    {
        return AttachmentDto.builder()
                .fileId(fileId)
                .fileName(fileName)
                .build();
    }
}
