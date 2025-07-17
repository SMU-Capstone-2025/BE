package com.capstone.domain.file.dto;


import lombok.Builder;

@Builder
public record  FileResponse(
        String fileId,
        String fileName

) {
    public static FileResponse from(String fileId, String fileName){

        return FileResponse.builder()
                .fileId(fileId)
                .fileName(fileName)
                .build();
    }
}
