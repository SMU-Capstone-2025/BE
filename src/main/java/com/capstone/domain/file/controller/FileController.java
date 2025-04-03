package com.capstone.domain.file.controller;

import com.capstone.docs.FileControllerDocs;
import com.capstone.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@CrossOrigin("*")
public class FileController implements FileControllerDocs {
    private final FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(file);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileId") String fileId) throws IOException {
        return fileService.download(fileId);
    }

    @DeleteMapping("delete")
    public void deleteFile(@RequestParam("fileId") String fileId){
        fileService.delete(fileId);
    }
}
