package com.capstone.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileTypes {
    public static final List<String> SUPPORTED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/gif");
    public static final List<String> SUPPORTED_DOCUMENT_TYPES = List.of("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain");
    public static final List<String> SUPPORTED_SPREADSHEET_TYPES = List.of("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    public static boolean SUPPORTED_TYPES(String fileType) {
        return SUPPORTED_IMAGE_TYPES.contains(fileType) || SUPPORTED_DOCUMENT_TYPES.contains(fileType) || SUPPORTED_SPREADSHEET_TYPES.contains(fileType);
    }
}
