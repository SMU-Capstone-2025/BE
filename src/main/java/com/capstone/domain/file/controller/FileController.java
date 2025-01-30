package com.capstone.domain.file.controller;

import com.capstone.domain.file.service.FileService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@CrossOrigin("*")
public class FileController {
    private final FileService fileService;
    private final GridFsTemplate gridFsTemplate;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.uploadFile(file);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileId") String fileId) throws IOException {
        return fileService.downloadFile(fileId);
    }

    @DeleteMapping("delete")
    public void deleteFile(@RequestParam("fileId") String fileId){
        fileService.deleteFile(fileId);
    }


    //TODO: 파일이 여러 개인 경우에도 동작하도록 수정 & 서비스 레이어로 이동.
//    @GetMapping("/get")
//    public ResponseEntity<List<Resource>> getFile(@RequestParam List<String> fileIds) throws IOException {
//        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").in(fileIds)));
//
//        if (file == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        GridFsResource resource = gridFsTemplate.getResource(file);
//
//        // 파일의 MIME 타입 가져오기
//        String contentType = file.getMetadata() != null && file.getMetadata().containsKey("_contentType")
//                ? file.getMetadata().get("_contentType").toString()
//                : "application/octet-stream";
//
//        // HTTP 응답 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(contentType));
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(resource);
//    }
}
