package com.capstone.domain.file.service;

import com.capstone.domain.file.common.FileMessages;
import com.capstone.domain.file.common.FileTypes;
import com.capstone.domain.file.exception.InvalidFileException;
import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.entity.Version;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.domain.task.service.TaskService;
import com.capstone.domain.task.util.VersionUtil;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.mongodb.client.gridfs.model.GridFSFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final GridFsTemplate gridFsTemplate;
    private final TaskRepository taskRepository;

    public String upload(String taskId, MultipartFile file) throws IOException {
        // Task 전체를 기준으로 가져옴
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.TASK_NOT_FOUND));

        // 현재 버전에 해당하는 Version 객체를 versionHistory 안에서 직접 가져옴
        Version targetVersion = VersionUtil.getCurrentVersionEntity(task);

        if (!FileTypes.SUPPORTED_TYPES(file.getContentType())) {
            throw new GlobalException(ErrorStatus.FILE_NOT_SUPPORTED);
        }

        if (file.isEmpty()) {
            throw new GlobalException(ErrorStatus.FILE_EMPTY);
        }

        // GridFS에 파일 저장
        ObjectId objectId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        // Version 안에 attachment 추가
        targetVersion.addAttachment(objectId.toHexString());

        // 전체 Task 저장 (MongoDB는 embedded document만 직접 저장 불가)
        taskRepository.save(task);

        return objectId.toHexString();
    }

    public ResponseEntity<Resource> download(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(fileId))
        );


        GridFsResource resource = gridFsTemplate.getResource(file);

        if (!resource.exists()){
            throw new GlobalException(ErrorStatus.FILE_NOT_FOUND);
        }

        String contentType = file.getMetadata() != null && file.getMetadata().containsKey("_contentType")
                ? file.getMetadata().get("_contentType").toString()
                : "application/octet-stream";

        String filename = file.getFilename();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    public String delete(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(fileId))
        );
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
        return file.getFilename();
    }
}
